package cn.edu.sustech.ces.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EntityManager entityManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserTicketRepository userTicketRepository;
    @InjectMocks
    private OrderService orderService;

    private UUID orderId;
    private UUID ticketId;
    private Order order;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        order = new Order();
        order.setId(orderId);
        order.setTicketId(ticketId);
        order.setStatus(OrderStatus.UNPAID);
        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setLockAmount(1);

    }

    @Test
    void cancelOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(entityManager.find(Ticket.class, ticketId, LockModeType.OPTIMISTIC)).thenReturn(ticket);
        Order result = orderService.cancelOrder(orderId);
        assertNotNull(result);
        assertEquals(OrderStatus.CANCELED, result.getStatus());
        verify(entityManager).merge(ticket);
        verify(orderRepository).save(order);
    }

    @Test
    void testSaveOrder() {
        Order order = new Order();
        orderService.saveOrder(order);
        verify(orderRepository).save(order);
    }

    @Test
    void testGetOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Order foundOrder = orderService.getOrder(orderId);
        assertEquals(order, foundOrder);
        verify(orderRepository).findById(orderId);
    }


    @Test
    void testPayOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setPayerId(UUID.randomUUID());
        order.setTicketId(UUID.randomUUID());
        order.setStatus(OrderStatus.UNPAID);

        User user = new User();
        user.setId(order.getPayerId());
        user.setUserTickets(new ArrayList<>());

        Ticket ticket = new Ticket();
        ticket.setId(order.getTicketId());
        ticket.setSoldAmount(0);
        UserTicket userTicket = new UserTicket();
        userTicket.setId(UUID.randomUUID());
        when(entityManager.find(Ticket.class, ticket.getId(), LockModeType.OPTIMISTIC)).thenReturn(ticket);
        when(entityManager.merge(any(Ticket.class))).thenReturn(ticket);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userTicketRepository.save(any(UserTicket.class))).thenReturn(userTicket);

        Order paidOrder = orderService.payOrder(user, order);

        assertNotNull(paidOrder);
        assertEquals(OrderStatus.PAID, paidOrder.getStatus());
        verify(orderRepository).save(order);
        verify(userTicketRepository).save(any(UserTicket.class));
        assertTrue(user.getUserTickets().contains(userTicket.getId()));
    }


}
