package cn.edu.sustech.ces.scheduling;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.repository.EventRepository;
import cn.edu.sustech.ces.repository.OrderRepository;
import cn.edu.sustech.ces.service.EventService;
import cn.edu.sustech.ces.service.GlobalSettingService;
import cn.edu.sustech.ces.service.OrderService;
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
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final GlobalSettingService globalSettingService;

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

    @Scheduled(fixedRate = 1000)
    public void cancelOrder() {
        long expireTime = Long.parseLong(globalSettingService.getSetting(GlobalSettingService.ORDER_EXPIRE_TIME));
        long advanceTime = 0;
        List<Order> orders = orderRepository.findAllByStatusAndOrderCreateTimeBefore(OrderStatus.UNPAID, System.currentTimeMillis() - expireTime - advanceTime);
        orders.forEach(orderService::cancelOrder);
    }


}
