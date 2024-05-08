package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.service.GlobalSettingService;
import cn.edu.sustech.ces.service.TicketService;
import cn.edu.sustech.ces.service.minio.MinioService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import cn.edu.sustech.ces.service.EventService;

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
    public ResponseEntity<?> createEvent(@RequestBody JSONObject request) {

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
        event.setStatus(EventStatus.EDITING);
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
            ticket.setLockAmount(soldAmount);

            tickets.add(ticket);

        }

        if (tickets.isEmpty()) {
            ResponseEntity.badRequest().body("No Tickets");
        }

        event = eventService.saveEvent(event);

        for (Ticket ticket : tickets) {
            ticket.setEventId(event.getId());
            ticket = ticketService.saveTicket(ticket);
            event.getTickets().add(ticket.getId());
        }

        event = eventService.saveEvent(event);

        return ResponseEntity.ok(event);

    }

    @PostMapping("/update-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateEvent(@RequestParam UUID eventId, @RequestBody JSONObject eventJsonObject) {

        User user = CESUtils.getAuthorizedUser();
        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.badRequest().body("Event Not Found");
        }

        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN && !event.getPublisher().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (event.getStatus() == EventStatus.PENDING || event.getStatus() == EventStatus.IN_PROGRESS || event.getStatus() == EventStatus.FINISHED) {
            return ResponseEntity.badRequest().body("Event is not editable");
        }

        if (eventJsonObject.containsKey("title")) {
            event.setTitle(eventJsonObject.getString("title"));
        }

        if (eventJsonObject.containsKey("start_time")) {
            event.setStartTime(eventJsonObject.getLong("start_time"));
        }

        if (eventJsonObject.containsKey("end_time")) {
            event.setEndTime(eventJsonObject.getLong("end_time"));
        }

        if (eventJsonObject.containsKey("document_url")) {
            event.setDocumentUrl(eventJsonObject.getString("document_url"));
        }

        if (eventJsonObject.containsKey("image_url")) {
            event.setImageUrl(eventJsonObject.getString("image_url"));
        }

        if (eventJsonObject.containsKey("latitude")) {
            event.setLatitude(eventJsonObject.getInteger("latitude"));
        }

        if (eventJsonObject.containsKey("longitude")) {
            event.setLongitude(eventJsonObject.getInteger("longitude"));
        }

        if (eventJsonObject.containsKey("location_name")) {
            event.setLocationName(eventJsonObject.getString("location_name"));
        }

        if (eventJsonObject.containsKey("category_id")) {
            event.setCategoryId(eventJsonObject.getInteger("category_id"));
        }

        if (eventJsonObject.containsKey("tickets")) {
            List<Ticket> tickets = new ArrayList<>();
            JSONArray ticketsJson = eventJsonObject.getJSONArray("tickets");
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
                ticket.setLockAmount(soldAmount);
                tickets.add(ticket);
            }
            if (tickets.isEmpty()) {
                return ResponseEntity.badRequest().body("No Tickets");
            }
            event.getTickets().forEach(ticketService::deleteTicket);
            event.getTickets().clear();
            for (Ticket ticket : tickets) {
                ticket.setEventId(event.getId());
                ticket = ticketService.saveTicket(ticket);
                event.getTickets().add(ticket.getId());
            }
        }

        event.setStatus(EventStatus.EDITING);
        event = eventService.saveEvent(event);

        return ResponseEntity.ok(event);

    }

    @PostMapping("/explore-events-size")
    public ResponseEntity<Long> exploreEventsSize(HttpServletRequest request) {
        Set<EventStatus> statuses = Set.of(EventStatus.PENDING, EventStatus.IN_PROGRESS);
        String title = null;
        Integer categoryId = null;
        if (request.getParameter("title") != null) {
            title = request.getParameter("title");
        }
        if (request.getParameter("category_id") != null) {
            categoryId = Integer.parseInt(request.getParameter("category_id"));
        }
        return ResponseEntity.ok(eventService.countEventsByFilter(title, categoryId, statuses, null));
    }

    @PostMapping("/explore-events")
    public ResponseEntity<?> exploreEvents(@RequestParam int page, HttpServletRequest request, @RequestParam(required = false, defaultValue = "10") int size) {
        if (size < 0 || page < 0) {
            return ResponseEntity.badRequest().body("Invalid page or size");
        }
        Set<EventStatus> statuses = Set.of(EventStatus.PENDING, EventStatus.IN_PROGRESS);
        Pageable pageable = PageRequest.of(page, size);
        String title = null;
        Integer categoryId = null;
        if (request.getParameter("title") != null) {
            title = request.getParameter("title");
        }
        if (request.getParameter("category_id") != null) {
            categoryId = Integer.parseInt(request.getParameter("category_id"));
        }
        Page<Event> events = eventService.getEventsByFilter(pageable, title, categoryId, statuses, null);
        List<Event> eventList = events.getContent();
        return ResponseEntity.ok(eventList);
    }


    @PostMapping("/list-events-size")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Long> listEventsSize(HttpServletRequest request) {

        User user = CESUtils.getAuthorizedUser();

        String title = null;
        Integer categoryId = null;
        UUID publisher = null;
        Set<EventStatus> statuses = null;
        if (request.getParameter("title") != null) {
            title = request.getParameter("title");
        }
        if (request.getParameter("category_id") != null) {
            categoryId = Integer.parseInt(request.getParameter("category_id"));
        }

        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN) {
            publisher = user.getId();
        } else {
            if (request.getParameter("publisher") != null) {
                publisher = UUID.fromString(request.getParameter("publisher"));
            }
        }

        if (request.getParameter("statuses") != null) {
            statuses = new HashSet<>();
            String[] statusStrings = request.getParameter("statuses").split(",");
            for (String statusString : statusStrings) {
                statuses.add(EventStatus.valueOf(statusString));
            }
        }
        return ResponseEntity.ok(eventService.countEventsByFilter(title, categoryId, statuses, publisher));
    }

    @PostMapping("/list-events")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> listEvents(@RequestParam int page, @RequestParam(required = false, defaultValue = "10") int size, HttpServletRequest request) {
        if (size < 0 || page < 0) {
            return ResponseEntity.badRequest().body("Invalid page or size");
        }
        User user = CESUtils.getAuthorizedUser();
        String title = null;
        Integer categoryId = null;
        UUID publisher = null;
        Set<EventStatus> statuses = null;
        if (request.getParameter("title") != null) {
            title = request.getParameter("title");
        }
        if (request.getParameter("category_id") != null) {
            categoryId = Integer.parseInt(request.getParameter("category_id"));
        }
        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN) {
            publisher = user.getId();
        } else {
            if (request.getParameter("publisher") != null) {
                publisher = UUID.fromString(request.getParameter("publisher"));
            }
        }

        if (request.getParameter("statuses") != null) {
            statuses = new HashSet<>();
            String[] statusStrings = request.getParameter("statuses").split(",");
            for (String statusString : statusStrings) {
                statuses.add(EventStatus.valueOf(statusString));
            }
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventService.getEventsByFilter(pageable, title, categoryId, statuses, publisher);
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

    @PostMapping("/publish-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> publishEvent(@RequestParam UUID eventId) {

        User user = CESUtils.getAuthorizedUser();

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.badRequest().body("Event Not Found");
        }

        if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN && !event.getPublisher().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        event.setStatus(EventStatus.AUDITING);
        event = eventService.saveEvent(event);
        return ResponseEntity.ok(event);

    }

    @PostMapping("/get-event")
    public ResponseEntity<?> getEvent(@RequestParam UUID eventId) {

        Event event = eventService.getEventById(eventId);

        if (event == null) {
            return ResponseEntity.badRequest().body("Event Not Found");
        }

        if (event.getStatus() == EventStatus.AUDITING || event.getStatus() == EventStatus.EDITING) {
            User user = CESUtils.getAuthorizedUser();
            if (user == null || user.getPermissionGroup() == PermissionGroup.USER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (user.getPermissionGroup() == PermissionGroup.DEPARTMENT_ADMIN && !event.getPublisher().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

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