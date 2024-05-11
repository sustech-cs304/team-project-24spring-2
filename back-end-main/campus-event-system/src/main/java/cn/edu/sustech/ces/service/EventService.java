package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.repository.UserRepository;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

    public Event saveEvent(Event event) {
        eventRepository.save(event);
        return event;
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).getContent();
    }

    public List<Event> getEventsByFilter(Pageable pageable, String title, String category, Set<EventStatus> statuses, UUID publisher) {
        List<Event> events = null;
        if (publisher == null) {
            events = eventRepository.findAllByStatusIn(statuses);
        } else {
            events = eventRepository.findAllByStatusInAndPublisher(statuses, publisher);
        }
        events = events.stream()
                .filter(event -> title == null || title.isEmpty() || event.getTitle().contains(title))
                .filter(event -> category == null || event.getCategory().equalsIgnoreCase(category))
                .toList();
        if (pageable == null)
            return events;
        return CESUtils.getPage(pageable, events);
    }

    public List<Event> getEventsByPublisher(Pageable pageable, User user) {
        if (pageable == null)
            return eventRepository.findAllByPublisher(user.getId());
        return eventRepository.findAllByPublisher(pageable, user.getId()).getContent();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public Long countEvents() {
        return eventRepository.count();
    }

    public long countEventsByPublisher(User user) {
        return eventRepository.countByPublisher(user.getId());
    }

    public long countEventsByFilter(String title, String category, Set<EventStatus> statuses, UUID publisher) {
        return getEventsByFilter(null, title, category, statuses, publisher).size();
    }




}
