package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.UserEnrollEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserEnrollEventRepository extends CrudRepository<UserEnrollEvent, UUID> {
    List<UserEnrollEvent> findByUserId(UUID userId);
}
