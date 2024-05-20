package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Comment;
import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.EventStatus;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.service.CommentService;
import cn.edu.sustech.ces.service.EventService;
import cn.edu.sustech.ces.service.GlobalSettingService;
import cn.edu.sustech.ces.service.minio.MinioService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final GlobalSettingService globalSettingService;
    private final MinioService minioService;
    private final EventService eventService;

    @PostMapping("/make-comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> makeComment(@RequestBody JSONObject commentRequest) {

        User user = CESUtils.getAuthorizedUser();

        UUID eventId = UUID.fromString(commentRequest.getString("event_id"));
        Event event = eventService.getEventById(eventId);
        if (event == null || event.getStatus() == EventStatus.AUDITING) {
            return ResponseEntity.badRequest().body("Event not found or not commentable");
        }
        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setUserId(user.getId());
        comment.setContent(commentRequest.getString("content"));
        comment.setCreateTime(System.currentTimeMillis());
        return ResponseEntity.ok(commentService.saveComment(comment));
    }

    @PostMapping("/delete-comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(@RequestParam UUID commentId) {

        User user = CESUtils.getAuthorizedUser();
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.badRequest().body("Comment not found");
        }
        if (user.getPermissionGroup().ordinal() < PermissionGroup.INSTITUTE_ADMIN.ordinal() && !comment.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this comment");
        }

        commentService.deleteComment(comment);
        minioService.deleteDirectory(minioService.getCommentBucket(), commentId.toString());

        return ResponseEntity.ok("Comment deleted");
    }

    @PostMapping("/get-comment")
    public ResponseEntity<?> getComment(@RequestParam UUID commentId) {

        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.badRequest().body("Comment not found");
        }

        return ResponseEntity.ok(comment);
    }

    @PostMapping("/get-page-comments")
    public ResponseEntity<?> getComments(@RequestParam UUID eventId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 0) {
            return ResponseEntity.badRequest().body("Invalid page or size");
        }
        if (size > Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.MAX_PAGE_SIZE))) {
            return ResponseEntity.badRequest().body("Size exceeds limit");
        }
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentService.getCommentsByEventId(eventId, pageable));
    }

    @PostMapping("/get-comments-size")
    public ResponseEntity<Long> getCommentsSize(@RequestParam UUID eventId) {
        return ResponseEntity.ok(commentService.countCommentsByEventId(eventId));
    }

    @PostMapping("/get-comments")
    public ResponseEntity<?> getComments(@RequestParam UUID eventId) {
        return ResponseEntity.ok(commentService.getCommentsByEventId(eventId));
    }

    @PostMapping("/get-comment-attachments")
    public ResponseEntity<?> getCommentAttachments(@RequestParam UUID commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.badRequest().body("Comment not found");
        }
        List<Item> items = minioService.getItems(minioService.getCommentBucket(), commentId.toString());
        List<String> urls = items.stream().filter(item -> {
            Optional<String> suffix = Optional.ofNullable(item.objectName())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(item.objectName().lastIndexOf(".") + 1));
            if (suffix.isEmpty()) {
                return false;
            }
            suffix = Optional.of(suffix.get().toLowerCase());
            return suffix.get().equalsIgnoreCase("jpg") || suffix.get().equalsIgnoreCase("jpeg") || suffix.get().equalsIgnoreCase("png") || suffix.get().equalsIgnoreCase("mp4");
        }).map(item -> "/" + minioService.getCommentBucket() + "/" + item.objectName()).toList();
        return ResponseEntity.ok(urls);
    }

}
