package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.service.GlobalSettingService;
import cn.edu.sustech.ces.service.TicketService;
import cn.edu.sustech.ces.service.minio.MinioService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import cn.edu.sustech.ces.service.EventService;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;
    private final TicketService ticketService;
    private final MinioService minioService;
    private final GlobalSettingService globalSettingService;

    @PostMapping("/create-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Event> createEvent(@RequestBody JSONObject request) {

        User user = CESUtils.getAuthorizedUser();

        String title = request.getString("title");
        Long startTime = request.getLong("start_time");
        Long endTime = request.getLong("end_time");
        String documentUrl = request.getString("document_url");
        String imageUrl = request.getString("image_url");
        Integer latitude = request.getInteger("latitude");
        Integer longitude = request.getInteger("longitude");
        String locationName = request.getString("location_name");
        Integer categoryId = request.getInteger("category_id");

        Event event = new Event();
        event.setTitle(title);
        event.setLocation(latitude, longitude, locationName);
        event.setDocumentUrl(documentUrl);
        event.setImageUrl(imageUrl);
        event.setEndTime(endTime);
        event.setStartTime(startTime);
        event.setTitle(title);
        event.setStatus(EventStatus.AUDITING);
        event.setPublisher(user.getId());
        event.setTickets(new ArrayList<>());
        event.setCategoryId(categoryId);

        List<Ticket> tickets = new ArrayList<>();

        JSONArray ticketsJson = request.getJSONArray("tickets");
        for (int i = 0; i < ticketsJson.size(); i++) {

            JSONObject ticketJson = ticketsJson.getJSONObject(i);

            String description = ticketJson.getString("description");
            Double price = ticketJson.getDouble("price");
            Integer totalAmount = ticketJson.getInteger("total_amount");
            Integer soldAmount = 0;
            if (ticketJson.get("sold_amount") != null) {
                soldAmount = ticketJson.getInteger("sold_amount");
            }

            Ticket ticket = new Ticket();
            ticket.setDescription(description);

            ticket.setPrice(price);
            ticket.setTotalAmount(totalAmount);
            ticket.setSoldAmount(soldAmount);

            tickets.add(ticket);

        }

        if (tickets.isEmpty()) {
            ResponseEntity.badRequest().body("No Tickets");
        }

        event = eventService.saveEvent(event);

        for (Ticket ticket : tickets) {
            ticket.setEventId(event.getId());
            ticket = ticketService.createTicket(ticket);
            event.getTickets().add(ticket.getId());
        }

        event = eventService.saveEvent(event);

        return ResponseEntity.ok(event);

    }

    @PostMapping("/explore-events-size")
    public ResponseEntity<Long> exploreEventsSize() {
        return ResponseEntity.ok(eventService.countPendingAndInProgressEvent());
    }

    @PostMapping("/explore-events")
    public ResponseEntity<?> exploreEvents(@RequestParam int page, @RequestParam(required = false, defaultValue = "10") int size) {
        if (page < 0 || size < 0) {
            return ResponseEntity.badRequest().body("Invalid page or size");
        }
        if (size > Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.MAX_PAGE_SIZE))) {
            return ResponseEntity.badRequest().body("Size too large");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventService.getPendingAndInProgressEvent(pageable);
        List<Event> eventList = events.getContent();
        return ResponseEntity.ok(eventList);
    }

    @PostMapping("/list-events-size")
    public ResponseEntity<Long> listEventsSize() {
        return ResponseEntity.ok(eventService.countEvents());
    }

    @PostMapping("/list-events")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> listEvents(@RequestParam(required = false, defaultValue = "-1") int page, @RequestParam(required = false, defaultValue = "10") int size) {

        User user = CESUtils.getAuthorizedUser();

        if (page == -1) {
            if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN) {
                List<Event> events = eventService.getEventsByPublisher(user);
                return ResponseEntity.ok(events);
            }
            List<Event> events = eventService.getEvents();
            return ResponseEntity.ok(events);
        }
        if (size < 0 || page < 0) {
            return ResponseEntity.badRequest().body("Invalid page or size");
        }
        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> events = eventService.getEventsByPublisher(pageable, user);
            List<Event> eventList = events.getContent();
            return ResponseEntity.ok(eventList);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventService.getEvents(pageable);
        List<Event> eventList = events.getContent();
        return ResponseEntity.ok(eventList);
    }

    //TODO: Delete all relative tickets and information

    @PostMapping("/delete-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteEvent(@RequestParam UUID eventId) {

        User user = CESUtils.getAuthorizedUser();

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.badRequest().body("Event Not Found");
        }

        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN && !event.getPublisher().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        event.getTickets().forEach(ticketService::deleteTicket);

        eventService.deleteEvent(event);

        return ResponseEntity.ok(event);

    }

    @PostMapping("/audit-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Event> auditEvent(@RequestParam UUID eventId) {

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        event.setStatus(EventStatus.PENDING);

        if (event.getStartTime() > System.currentTimeMillis()) {
            event.setStatus(EventStatus.IN_PROGRESS);
        }

        if (event.getEndTime() < System.currentTimeMillis()) {
            event.setStatus(EventStatus.FINISHED);
        }

        event = eventService.saveEvent(event);

        return ResponseEntity.ok(event);

    }


}