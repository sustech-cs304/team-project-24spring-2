package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.repository.UserRepository;
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

    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findAllByTitle(title);
    }

    public Event saveEvent(Event event) {
        eventRepository.save(event);
        return event;
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Page<Event> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Page<Event> getEventsByFilter(Pageable pageable, String title, Integer categoryId, Set<EventStatus> statuses, UUID publisher) {
        if (title == null && categoryId == null && publisher == null) {
            return eventRepository.findAllByStatusIn(pageable, statuses);
        } else if (title == null && categoryId == null) {
            return eventRepository.findAllByPublisher(pageable, publisher);
        } else if (title == null && publisher == null) {
            return eventRepository.findAllByCategoryIdAndStatusIn(pageable, categoryId, statuses);
        } else if (categoryId == null && publisher == null) {
            return eventRepository.findAllByTitleContainingAndStatusIn(pageable, title, statuses);
        } else if (title == null) {
            return eventRepository.findAllByCategoryIdAndPublisherAndStatusIn(pageable, categoryId, publisher, statuses);
        } else if (categoryId == null) {
            return eventRepository.findAllByTitleContainingAndPublisherAndStatusIn(pageable, title, publisher, statuses);
        } else if (publisher == null) {
            return eventRepository.findAllByTitleContainingAndCategoryIdAndStatusIn(pageable, title, categoryId, statuses);
        } else {
            return eventRepository.findAllByTitleContainingAndCategoryIdAndPublisherAndStatusIn(pageable, title, categoryId, publisher, statuses);
        }
    }

    public List<Event> getEventsByPublisher(User user) {
        return eventRepository.findAllByPublisher(user.getId());
    }

    public Page<Event> getEventsByPublisher(Pageable pageable, User user) {
        return eventRepository.findAllByPublisher(pageable, user.getId());
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

    public long countEventsByFilter(String title, Integer categoryId, Set<EventStatus> statuses, UUID publisher) {
        if (title == null && categoryId == null && publisher == null) {
            return eventRepository.countByStatusIn(statuses);
        } else if (title == null && categoryId == null) {
            return eventRepository.countByPublisher(publisher);
        } else if (title == null && publisher == null) {
            return eventRepository.countByCategoryIdAndStatusIn(categoryId, statuses);
        } else if (categoryId == null && publisher == null) {
            return eventRepository.countByTitleContainingAndStatusIn(title, statuses);
        } else if (title == null) {
            return eventRepository.countByCategoryIdAndPublisherAndStatusIn(categoryId, publisher, statuses);
        } else if (categoryId == null) {
            return eventRepository.countByTitleContainingAndPublisherAndStatusIn(title, publisher, statuses);
        } else if (publisher == null) {
            return eventRepository.countByTitleContainingAndCategoryIdAndStatusIn(title, categoryId, statuses);
        } else {
            return eventRepository.countByTitleContainingAndCategoryIdAndPublisherAndStatusIn(title, categoryId, publisher, statuses);
        }
    }




}
