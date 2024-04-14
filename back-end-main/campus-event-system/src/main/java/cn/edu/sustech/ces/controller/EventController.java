package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.service.LocationService;
import cn.edu.sustech.ces.service.UserService;
import cn.edu.sustech.ces.utils.CreationResponse;
import cn.edu.sustech.ces.utils.EventDTO;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/create-event")
    public String createEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO.getStartTime().isAfter(eventDTO.getEndTime())) {
            CreationResponse response = new CreationResponse(false, "Start time should be before end time");
            return JSON.toJSONString(response);
        } else if (eventDTO.getAvailableCapacity() < 0) {
            CreationResponse response = new CreationResponse(false, "Available capacity should be greater than 0");
            return JSON.toJSONString(response);
        } else if (eventDTO.getCurrentCapacity() < 0) {
            CreationResponse response = new CreationResponse(false, "Current capacity should be greater than 0");
            return JSON.toJSONString(response);
        } else if (eventDTO.getCurrentCapacity() > eventDTO.getAvailableCapacity()) {
            CreationResponse response = new CreationResponse(false, "Current capacity should be less than available capacity");
            return JSON.toJSONString(response);
        } else if (eventDTO.getPublisherId() == null) {
            CreationResponse response = new CreationResponse(false, "Publisher ID should not be null");
            return JSON.toJSONString(response);
        } else if (eventDTO.getLocationId() == null) {
            CreationResponse response = new CreationResponse(false, "Location ID should not be null");
            return JSON.toJSONString(response);
        }
        int publisherId = eventDTO.getPublisherId();
        User publisher = userService.getUserById(publisherId);
        if (publisher == null) {
            CreationResponse response = new CreationResponse(false, "Publisher not found");
            return JSON.toJSONString(response);
        }
        UUID locationId = eventDTO.getLocationId();
        Location location = locationService.getLocationById(locationId);
        if (location == null) {
            CreationResponse response = new CreationResponse(false, "Location not found");
            return JSON.toJSONString(response);
        }
        Event event = new Event(
            eventDTO.getTitle(),
            publisherId,
            eventDTO.getPublishTime(),
            eventDTO.getStartTime(),
            eventDTO.getEndTime(),
            eventDTO.getDescription(),
            locationId,
            eventDTO.getAvailableCapacity(),
            eventDTO.getCurrentCapacity()
        );
        event = eventRepository.save(event);
        CreationResponse response = new CreationResponse(true, event.getId().toString());
        return JSON.toJSONString(response);
    }

}
