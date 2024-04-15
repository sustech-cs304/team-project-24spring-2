package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.utils.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.edu.sustech.ces.service.EventService;

import java.util.UUID;

@RestController
@RequestMapping("/api/activity")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public String createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEventService(eventDTO);
    }
    @GetMapping("/query")
    public String queryEvent(@RequestParam("id") UUID id) {
        return eventService.queryEventService(id);
    }
    @GetMapping("/delete")
    public String deleteEvent(@RequestParam("id") UUID id) {
        return eventService.deleteEventService(id);
    }
}