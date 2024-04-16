package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.UserEvent;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserEventRepository extends ListCrudRepository<UserEvent, Long> {

    public List<UserEvent> findAllByEventId(UUID eventId);
    public List<UserEvent> findAllByUserId(UUID userId);

}
