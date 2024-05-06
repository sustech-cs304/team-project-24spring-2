package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Ticket createTicket(Ticket ticket) {
        ticketRepository.save(ticket);
        return ticket;
    }

    public Ticket getTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    public void deleteTicket(UUID uuid) {
        ticketRepository.deleteById(uuid);
    }
}
