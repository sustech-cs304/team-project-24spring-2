package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.UserTicket;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserTicketRepository extends ListCrudRepository<UserTicket, UUID> {

    public List<UserTicket> findByTicketId(UUID ticketId);

}
