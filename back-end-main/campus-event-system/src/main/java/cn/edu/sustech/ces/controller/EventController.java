package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.security.CESUserDetails;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import cn.edu.sustech.ces.service.EventService;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    @PostMapping("/create-event")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Event> createEvent(@RequestBody JSONObject request) {

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Event event = eventService.createEvent(
                request.getString("title"),
                user.getId(),
                request.getLong("publish_time"),
                request.getLong("start_time"),
                request.getLong("end_time"),
                request.getString("document_url"),
                request.getString("image_url"),
                request.getInteger("available_capacity"),
                request.getInteger("altitude"),
                request.getInteger("longitude")
        );

        return ResponseEntity.ok(event);

    }

    @PostMapping("/list-events")
    public ResponseEntity<List<Event>> listEvents() {
        List<Event> events = eventService.getEvents();
        return ResponseEntity.ok(events);
    }

}