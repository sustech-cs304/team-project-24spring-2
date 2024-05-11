package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/get-ticket")
    public ResponseEntity<?> getTicketById(@RequestParam UUID ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

}
