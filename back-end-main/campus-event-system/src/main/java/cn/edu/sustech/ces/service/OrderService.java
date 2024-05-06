package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order makeOrder(String name, Ticket ticket, double price, User payer) {
        Order order = new Order();
        order.setPayerId(payer.getId());
        order.setTicketId(ticket.getId());
        order.setName(name);
        order.setPrice(price);
        order.setStatus(OrderStatus.UNPAID);
        orderRepository.save(order);
        return order;
    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

}
