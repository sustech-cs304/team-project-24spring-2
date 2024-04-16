package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.UserEvent;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.repository.UserEventRepository;
import cn.edu.sustech.ces.repository.UserRepository;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserEventRepository userEventRepository;

    public List<UUID> getParticipants(UUID eventId) {
        List<UserEvent> userEvents = userEventRepository.findAllByEventId(eventId);
        return userEvents.stream().map(UserEvent::getUserId).toList();
    }

    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findAllByTitle(title);
    }

    public Event createEvent(String title, UUID publisher, Long publishTime, Long startTime, Long endTime, String documentUrl, String imageUrl, Integer availableCapacity, Integer altitude, Integer longitude) {
        Event event = new Event();
        event.setTitle(title);
        event.setPublisher(publisher);
        event.setPublishTime(publishTime);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setDocumentUrl(documentUrl);
        event.setImageUrl(imageUrl);
        event.setAvailableCapacity(availableCapacity);
        event.setLocation(altitude, longitude);
        event.setStatus(EventStatus.AUDITING);
        eventRepository.save(event);
        return event;
    }

}
