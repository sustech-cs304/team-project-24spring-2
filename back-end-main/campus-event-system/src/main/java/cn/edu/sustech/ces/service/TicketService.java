package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
        return ticket;
    }

    @Cacheable("ticket")
    public Ticket getTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    @Transactional
    public void deleteTicket(UUID uuid) {
        ticketRepository.deleteById(uuid);
    }
}
