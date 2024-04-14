package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.repository.LocationRepository;
import cn.edu.sustech.ces.utils.CreationResponse;
import cn.edu.sustech.ces.utils.EventDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private EventRepository eventRepository;
    public String createEventService(EventDTO eventDTO) {
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
        UUID publisherId = eventDTO.getPublisherId();
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

    public String queryEventService(UUID id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            CreationResponse response = new CreationResponse(false, "Event not found");
            return JSON.toJSONString(response);
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", event);
        return JSON.toJSONString(jsonResponse);
    }

    public String deleteEventService(UUID id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            // No event found to delete
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Event not found");
            return JSON.toJSONString(jsonResponse);
        }
        eventRepository.delete(event);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "Event deleted successfully");
        return JSON.toJSONString(jsonResponse);
    }
}
