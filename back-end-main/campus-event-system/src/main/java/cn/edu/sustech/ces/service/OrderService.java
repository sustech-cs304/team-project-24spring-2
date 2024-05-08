package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.entity.UserTicket;
import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.repository.OrderRepository;
import cn.edu.sustech.ces.repository.TicketRepository;
import cn.edu.sustech.ces.repository.UserRepository;
import cn.edu.sustech.ces.repository.UserTicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final UserTicketRepository userTicketRepository;
    private final EntityManager entityManager;
    private final ConcurrentHashMap<UUID, Lock> ticketLocks = new ConcurrentHashMap<>();

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getUserOrders(UUID userId) {
        return orderRepository.findAllByPayerId(userId);
    }

    public boolean anyUnpaidOrder(UUID userId) {
        return orderRepository.findFirstByPayerIdAndStatusIn(userId, Set.of(OrderStatus.UNPAID)).isPresent();
    }

    @Transactional
    public Order cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return cancelOrder(order);
    }

    @Transactional
    public Order cancelOrder(Order order) {
        if (order == null) {
            return null;
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            return null;
        }
        order.setStatus(OrderStatus.CANCELED);
        Lock lock = ticketLocks.computeIfAbsent(order.getTicketId(), k -> new ReentrantLock());
        try {
            lock.lock();
            Ticket ticket = entityManager.find(Ticket.class, order.getTicketId(), LockModeType.OPTIMISTIC);
            if (ticket == null) {
                return null;
            }
            ticket.setLockAmount(ticket.getLockAmount() - 1);
            entityManager.merge(ticket);
            order = orderRepository.save(order);
        } finally {
            lock.unlock();
        }
        return order;
    }

    @Transactional
    public Order payOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }
        User user = userRepository.findById(order.getPayerId()).orElse(null);
        return payOrder(user, order);
    }

    @Transactional
    public Order payOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        return payOrder(user, order);
    }

    @Transactional
    public Order payOrder(User user, Order order) {
        if (user == null || order == null) {
            return null;
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            return null;
        }
        Lock lock = ticketLocks.computeIfAbsent(order.getTicketId(), k -> new ReentrantLock());
        try {
            lock.lock();
            order.setStatus(OrderStatus.PAID);
            Ticket ticket = entityManager.find(Ticket.class, order.getTicketId(), LockModeType.OPTIMISTIC);
            int number = ticket.getSoldAmount();
            ticket.setSoldAmount(number + 1);
            entityManager.merge(ticket);
            order = orderRepository.save(order);
            UserTicket userTicket = new UserTicket();
            userTicket.setTicketId(order.getTicketId());
            userTicket.setNumber(number);
            userTicket = userTicketRepository.save(userTicket);
            user.getUserTickets().add(userTicket.getId());
            userRepository.save(user);
            return order;
        } catch (Exception e) {
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public Order lockAndMakeOrder(UUID userId, UUID ticketId) {
        Lock lock = ticketLocks.computeIfAbsent(ticketId, k -> new ReentrantLock());
        try {
            if (!lock.tryLock(10, TimeUnit.SECONDS)) {
                return null;
            }
            Ticket ticket = entityManager.find(Ticket.class, ticketId, LockModeType.OPTIMISTIC);
            if (ticket == null) {
                return null;
            }
            try {
                if (ticket.getTotalAmount() - ticket.getLockAmount() <= 0) {
                    return null;
                }
                ticket.setLockAmount(ticket.getLockAmount() + 1);
                entityManager.merge(ticket);
            } catch (Exception e) {
                return null;
            }
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.unlock();
        }
        Order order = new Order();
        order.setPayerId(userId);
        order.setTicketId(ticketId);
        order.setStatus(OrderStatus.UNPAID);
        order = orderRepository.save(order);
        return order;
    }

}
