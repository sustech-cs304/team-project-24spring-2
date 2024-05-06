package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Ticket;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends ListCrudRepository<Ticket, UUID> {
}
