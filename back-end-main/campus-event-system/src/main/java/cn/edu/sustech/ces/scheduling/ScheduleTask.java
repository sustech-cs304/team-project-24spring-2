package cn.edu.sustech.ces.scheduling;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class ScheduleTask {

    private final EventRepository eventRepository;

    @Scheduled(cron = "0 * * * * *")
    public void checkEventType() {
        List<Event> events = eventRepository.findAllByStatusInAndStartTimeBefore(Set.of(EventStatus.PENDING), System.currentTimeMillis());
        events.forEach(event -> {
            event.setStatus(EventStatus.IN_PROGRESS);
            eventRepository.save(event);
        });
        events = eventRepository.findAllByStatusInAndEndTimeBefore(Set.of(EventStatus.IN_PROGRESS, EventStatus.PENDING), System.currentTimeMillis());
        events.forEach(event -> {
            event.setStatus(EventStatus.FINISHED);
            eventRepository.save(event);
        });
    }


}
