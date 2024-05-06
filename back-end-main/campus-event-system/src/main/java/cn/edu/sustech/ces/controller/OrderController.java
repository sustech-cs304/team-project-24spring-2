package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.service.OrderService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @PostMapping("/make-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> makeOrder(@RequestBody JSONObject orderRequest) {

        /*
        TODO:
            1. Check if already have an order needed to be paid
            2. Check if the ticket is still available
            3. Lock the ticket, carefully handle the concurrency
         */

        return ResponseEntity.ok(null);
    }

    @PostMapping("/cancel-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> cancelOrder(@RequestParam UUID orderId) {

        /*
        TODO:
            1. Check existence of the order and the user is the owner
            2. Check if the order is cancellable
         */

        return ResponseEntity.ok(null);
    }

    @PostMapping("/get-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> getOrder(@RequestParam UUID orderId) {

        /*
        TODO:
            1. Check existence of the order and the user is the owner
            2. Return the order
         */

        return ResponseEntity.ok(null);
    }

}
