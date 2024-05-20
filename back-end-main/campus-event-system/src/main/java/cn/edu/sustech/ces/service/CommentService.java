package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Comment;
import cn.edu.sustech.ces.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getCommentById(UUID commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public List<Comment> getCommentsByEventId(UUID eventId, Pageable pageable) {
        return commentRepository.findByEventId(pageable, eventId);
    }

    public long countCommentsByEventId(UUID eventId) {
        return commentRepository.countByEventId(eventId);
    }

    public Object getCommentsByEventId(UUID eventId) {
        return commentRepository.findByEventId(eventId);
    }
}
