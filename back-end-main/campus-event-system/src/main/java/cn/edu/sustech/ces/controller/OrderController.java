package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.service.EventService;
import cn.edu.sustech.ces.service.OrderService;
import cn.edu.sustech.ces.service.TicketService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final TicketService ticketService;
    private final EventService eventService;

    @PostMapping("/make-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> makeOrder(@RequestBody JSONObject orderRequest) {

        User user = CESUtils.getAuthorizedUser();
        if (orderService.anyUnpaidOrder(user.getId())) {
            return ResponseEntity.badRequest().body("You have an unpaid order");
        }

        UUID ticketId = UUID.fromString(orderRequest.getString("ticketId"));
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            return ResponseEntity.badRequest().body("Ticket not found");
        }
        Event event = eventService.getEventById(ticket.getEventId());
        if (event == null) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        if (event.getStatus() != EventStatus.IN_PROGRESS && event.getStatus() != EventStatus.PENDING) {
            return ResponseEntity.badRequest().body("Event not in progress or pending");
        }
        Order order = orderService.lockAndMakeOrder(user.getId(), ticketId);
        if (order == null) {
            return ResponseEntity.badRequest().body("Failed to make order");
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/cancel-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelOrder(@RequestParam UUID orderId) {

        User user = CESUtils.getAuthorizedUser();
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().body("Order not found");
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            return ResponseEntity.badRequest().body("Order is not unpaid");
        }
        if (!order.getPayerId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("You are not the payer");
        }
        order = orderService.cancelOrder(order);
        if (order == null) {
            return ResponseEntity.badRequest().body("Failed to cancel order");
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/get-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrder(@RequestParam UUID orderId) {
        User user = CESUtils.getAuthorizedUser();
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().body("Order not found");
        }
        if (!order.getPayerId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("You are not the payer");
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/get-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrders() {
        User user = CESUtils.getAuthorizedUser();
        return ResponseEntity.ok(orderService.getUserOrders(user.getId()));
    }

}
