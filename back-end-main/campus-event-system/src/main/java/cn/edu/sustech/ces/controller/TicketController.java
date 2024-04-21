package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;

    public Ticket createTicket(Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    public Ticket getTicketById(UUID ticketId) {
        return ticketService.getTicketById(ticketId);
    }

}
