package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.service.TicketService;
import cn.edu.sustech.ces.utils.CESUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @PostMapping("/get-tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTickets() {
        User user = CESUtils.getAuthorizedUser();
        if (user.getUserTickets() == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(
                user.getUserTickets().stream().map(ticketService::getTicketById)
        );
    }

}
