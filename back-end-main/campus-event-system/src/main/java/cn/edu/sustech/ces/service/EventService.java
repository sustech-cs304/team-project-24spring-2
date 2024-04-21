package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.repository.UserRepository;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findAllByTitle(title);
    }

    public Event saveEvent(Event event) {
        eventRepository.save(event);
        return event;
    }

    public Event setEventStatus(UUID eventId, EventStatus status) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.setStatus(status);
            eventRepository.save(event);
        }
        return event;
    }

    public Page<Event> getPendingAndInProgressEvent(Pageable pageable) {

        return eventRepository.findAllByStatusIn(pageable, Set.of(EventStatus.PENDING, EventStatus.IN_PROGRESS));

    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Page<Event> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public List<Event> getEventsByPublisher(User user) {
        return eventRepository.findAllByPublisher(user.getId());
    }

    public Page<Event> getEventsByPublisher(Pageable pageable, User user) {
        return eventRepository.findAllByPublisher(pageable, user.getId());
    }

    public long countPendingAndInProgressEvent() {
        return eventRepository.countByStatusIn(Set.of(EventStatus.PENDING, EventStatus.IN_PROGRESS));
    }
}
