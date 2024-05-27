package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.entity.UserTicket;
import cn.edu.sustech.ces.repository.TicketRepository;
import cn.edu.sustech.ces.repository.UserTicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserTicketRepository userTicketRepository;

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

    public List<UserTicket> getSoldTickets(UUID ticketId) {
        return userTicketRepository.findByTicketId(ticketId);
    }

    public List<UserTicket> getSoldTickets(Ticket ticket) {
        if (ticket == null) {
            return new ArrayList<>();
        }
        return userTicketRepository.findByTicketId(ticket.getId());
    }
}
