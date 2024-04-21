package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.net.ContentHandler;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends ListCrudRepository<Event, UUID> {
    List<Event> findAllByTitle(String title);

    List<Event> findAllByPublisher(UUID publisher);
    List<Event> findAllByStatus(EventStatus status);

    Page<Event> findAllByStatusIn(Pageable pageable, Set<EventStatus> pending);

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByPublisher(Pageable pageable, UUID publisherId);
}

