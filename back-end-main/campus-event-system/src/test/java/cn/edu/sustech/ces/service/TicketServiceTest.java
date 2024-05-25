package cn.edu.sustech.ces.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void testSaveTicket() {
        UUID ticketId = UUID.randomUUID();
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket savedTicket = ticketService.saveTicket(ticket);

        assertNotNull(savedTicket);
        assertEquals(ticketId, savedTicket.getId());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testGetTicketById() {
        UUID ticketId = UUID.randomUUID();
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        Ticket foundTicket = ticketService.getTicketById(ticketId);

        assertNotNull(foundTicket);
        assertEquals(ticketId, foundTicket.getId());
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testDeleteTicket() {
        UUID ticketId = UUID.randomUUID();

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository).deleteById(ticketId);
    }
}
