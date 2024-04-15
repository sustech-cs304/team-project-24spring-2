package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.UserCreateEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserCreateEventRepository extends CrudRepository<UserCreateEvent, UUID> {
    List<UserCreateEvent> findByUserId(UUID userId);
}
