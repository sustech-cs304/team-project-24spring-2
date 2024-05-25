package cn.edu.sustech.ces.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cn.edu.sustech.ces.entity.Comment;
import cn.edu.sustech.ces.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private UUID commentId;
    private UUID eventId;

    @BeforeEach
    void setUp() {
        commentId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        comment = new Comment();
        comment.setId(commentId);
        comment.setEventId(eventId);
        comment.setContent("Sample comment");
    }

    @Test
    void testSaveComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.saveComment(comment);

        assertNotNull(savedComment);
        assertEquals(commentId, savedComment.getId());
        verify(commentRepository).save(comment);
    }

    @Test
    void testGetCommentById() {
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getCommentById(commentId);

        assertNotNull(foundComment);
        assertEquals(commentId, foundComment.getId());
        verify(commentRepository).findById(commentId);
    }

    @Test
    void testDeleteComment() {
        commentService.deleteComment(comment);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testGetCommentsByEventIdWithPageable() {
        Pageable pageable = mock(Pageable.class);
        List<Comment> comments = Arrays.asList(comment);
        Page<Comment> page = new PageImpl<>(comments, pageable, comments.size());

        when(commentRepository.findByEventId(pageable, eventId)).thenReturn(comments );

        List<Comment> resultComments = commentService.getCommentsByEventId(eventId, pageable);

        assertNotNull(resultComments);
        assertFalse(resultComments.isEmpty());
        assertEquals(commentId, resultComments.get(0).getId());
        verify(commentRepository).findByEventId(pageable, eventId);
    }

    @Test
    void testCountCommentsByEventId() {
        long expectedCount = 10;
        when(commentRepository.countByEventId(eventId)).thenReturn(expectedCount);

        long count = commentService.countCommentsByEventId(eventId);

        assertEquals(expectedCount, count);
        verify(commentRepository).countByEventId(eventId);
    }

    @Test
    void testGetCommentsByEventId() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findByEventId(eventId)).thenReturn(comments);

        List<Comment> resultComments = (List<Comment>) commentService.getCommentsByEventId(eventId);

        assertNotNull(resultComments);
        assertFalse(resultComments.isEmpty());
        assertEquals(commentId, resultComments.get(0).getId());
        verify(commentRepository).findByEventId(eventId);
    }
}
