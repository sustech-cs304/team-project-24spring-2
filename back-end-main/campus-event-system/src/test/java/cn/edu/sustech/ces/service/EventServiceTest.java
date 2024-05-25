package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void getEventByIdTest() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Event found = eventService.getEventById(eventId);

        verify(eventRepository).findById(eventId);
        assert found.equals(event);
    }

    @Test
    void saveEventTest() {
        Event event = new Event();
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event savedEvent = eventService.saveEvent(event);

        verify(eventRepository).save(event);
        assert savedEvent.equals(event);
    }

    @Test
    void getEventsTest() {
        Event event = new Event();
        when(eventRepository.findAll()).thenReturn(Collections.singletonList(event));

        List<Event> events = eventService.getEvents();

        verify(eventRepository).findAll();
        assert events.size() == 1;
        assert events.contains(event);
    }

    @Test
    void getEventsByPageTest() {
        Page<Event> page = new PageImpl<>(Arrays.asList(new Event(), new Event()));
        Pageable pageable = PageRequest.of(0, 2);
        when(eventRepository.findAll(pageable)).thenReturn(page);

        List<Event> events = eventService.getEvents(pageable);

        verify(eventRepository).findAll(pageable);
        assert events.size() == 2;
    }

}