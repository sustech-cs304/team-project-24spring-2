package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface EventRepository extends ListCrudRepository<Event, UUID> {
}

