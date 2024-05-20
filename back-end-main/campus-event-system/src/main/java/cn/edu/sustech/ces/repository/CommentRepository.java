package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends ListCrudRepository<Comment, UUID> {
    List<Comment> findByEventId(Pageable pageable, UUID eventId);

    long countByEventId(UUID eventId);

    List<Comment> findByEventId(UUID eventId);
}
