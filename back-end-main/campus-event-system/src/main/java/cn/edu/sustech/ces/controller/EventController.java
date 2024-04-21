package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.service.TicketService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/create-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Event> createEvent(@RequestBody JSONObject request) {

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        String title = request.getString("title");
        Long startTime = request.getLong("start_time");
        Long endTime = request.getLong("end_time");
        String documentUrl = request.getString("document_url");
        String imageUrl = request.getString("image_url");
        Integer altitude = request.getInteger("altitude");
        Integer longitude = request.getInteger("longitude");
        String locationName = request.getString("location_name");

        Event event = new Event();
        event.setTitle(title);
        event.setLocation(altitude, longitude, locationName);
        event.setDocumentUrl(documentUrl);
        event.setImageUrl(imageUrl);
        event.setEndTime(endTime);
        event.setStartTime(startTime);
        event.setTitle(title);
        event.setStatus(EventStatus.AUDITING);
        event.setPublisher(user.getId());
        event.setTickets(new ArrayList<>());

        List<Ticket> tickets = new ArrayList<>();

        JSONArray ticketsJson = request.getJSONArray("tickets");
        for (int i = 0; i < ticketsJson.size(); i++) {

            JSONObject ticketJson = ticketsJson.getJSONObject(i);

            String description = ticketJson.getString("description");
            Double price = ticketJson.getDouble("price");
            Integer totalAmount = ticketJson.getInteger("total_amount");

            Ticket ticket = new Ticket();
            ticket.setDescription(description);

            ticket.setPrice(price);
            ticket.setTotalAmount(totalAmount);
            ticket.setSoldAmount(0);

            tickets.add(ticket);

        }

        if (tickets.isEmpty()) {
            ResponseEntity.badRequest().build();
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

    @PostMapping("/explore-events")
    public ResponseEntity<List<Event>> exploreEvents(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Event> events = eventService.getPendingAndInProgressEvent(pageable);
        List<Event> eventList = events.getContent();
        return ResponseEntity.ok(eventList);
    }

    @PostMapping("/list-events")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<Event>> listEvents(@RequestParam(required = false, defaultValue = "-1") int page, @RequestParam(required = false, defaultValue = "-1") int size) {

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        if (page == -1 && size == -1) {
            if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN) {
                List<Event> events = eventService.getEventsByPublisher(user);
                return ResponseEntity.ok(events);
            }
            List<Event> events = eventService.getEvents();
            return ResponseEntity.ok(events);
        }
        if (page < 0 || size < 0) {
            return ResponseEntity.badRequest().build();
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

    @PostMapping("/audit-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Event> auditEvent(@RequestParam UUID eventId, @RequestParam EventStatus status) {

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN && !event.getPublisher().equals(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        event.setStatus(status);
        event = eventService.saveEvent(event);

        return ResponseEntity.ok(event);

    }


}