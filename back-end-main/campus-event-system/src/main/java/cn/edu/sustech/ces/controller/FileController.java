package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Comment;
import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.service.CommentService;
import cn.edu.sustech.ces.service.EventService;
import cn.edu.sustech.ces.service.GlobalSettingService;
import cn.edu.sustech.ces.service.minio.MinioService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileController {

    private final MinioService minioService;
    private final CommentService commentService;
    private final GlobalSettingService globalSettingService;
    private final EventService eventService;

    //TODO: add user upload file management

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadFile(@RequestParam String usage, HttpServletRequest request, @RequestBody MultipartFile file) {
        User user = CESUtils.getAuthorizedUser();
        if (usage.equalsIgnoreCase("comment")) {
            String commentIdParam = request.getParameter("commentId");
            if (commentIdParam == null) {
                return ResponseEntity.badRequest().body("Comment id is required");
            }
            UUID commentId = UUID.fromString(commentIdParam);
            Comment comment = commentService.getCommentById(commentId);
            if (comment == null) {
                return ResponseEntity.badRequest().body("Comment not found");
            }
            if (!comment.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
            }
            int weight = 0;
            String type = file.getContentType();
            if (type == null) {
                return ResponseEntity.badRequest().body("File type not supported");
            }
            Optional<String> suffix = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            if (suffix.isEmpty()) {
                return ResponseEntity.badRequest().body("File type not supported");
            }
            suffix = Optional.of(suffix.get().toLowerCase());
            if (type.startsWith("image")) {
                if (!suffix.get().equalsIgnoreCase("jpg") && !suffix.get().equalsIgnoreCase("jpeg") && !suffix.get().equalsIgnoreCase("png")) {
                    return ResponseEntity.badRequest().body("File type not supported");
                }
                weight += Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.COMMENT_IMAGE_WEIGHT));
            } else if (type.startsWith("video")) {
                if (!suffix.get().equalsIgnoreCase("mp4")) {
                    return ResponseEntity.badRequest().body("File type not supported");
                }
                weight += Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.COMMENT_VIDEO_WEIGHT));
            } else {
                return ResponseEntity.badRequest().body("File type not supported");
            }
            weight += countCommentWeight(commentId);
            if (weight > Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.COMMENT_MAX_WEIGHT))) {
                return ResponseEntity.badRequest().body("Comment attachment exceeds limit");
            }
            String fileName = commentId.toString() + "/" + UUID.randomUUID().toString() + "." + suffix.get();
            minioService.uploadFile(minioService.getCommentBucket(), fileName, file);
            return ResponseEntity.ok("/" + minioService.getCommentBucket() + "/" + fileName);
        }

        if (usage.equalsIgnoreCase("event")) {
            if (user.getPermissionGroup() == PermissionGroup.USER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
            }
            String eventIdParam = request.getParameter("eventId");
            if (eventIdParam == null) {
                return ResponseEntity.badRequest().body("Event id is required");
            }
            UUID eventId = UUID.fromString(eventIdParam);
            Event event = eventService.getEventById(eventId);
            if (event == null) {
                return ResponseEntity.badRequest().body("Event not found");
            }
            if (user.getPermissionGroup().ordinal() <= PermissionGroup.DEPARTMENT_ADMIN.ordinal() && !event.getPublisher().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
            }
            String type = file.getContentType();
            String fileName = file.getOriginalFilename();

            Optional<String> suffix = Optional.ofNullable(fileName)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(fileName.lastIndexOf(".") + 1));

            if (type == null || suffix.isEmpty() || (!type.startsWith("image") && !type.startsWith("text"))) {
                return ResponseEntity.badRequest().body("File type not supported");
            }

            String uploadFileName = "event/" + eventId.toString() + "/" + UUID.randomUUID().toString() + "." + suffix.get();
            String bucket = (type.startsWith("image") ? minioService.getImageBucket() : minioService.getDocumentBucket());
            minioService.uploadFile(bucket, uploadFileName, file);
            return ResponseEntity.ok("/" + bucket + "/" + uploadFileName);
        }

        if (usage.equalsIgnoreCase("user")) {
            if (user.getPermissionGroup() == PermissionGroup.USER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
            }

            String type = file.getContentType();
            // only allow image
            if (type == null || !type.startsWith("image")) {
                return ResponseEntity.badRequest().body("File type not supported");
            }
            Optional<String> suffix = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            if (suffix.isEmpty()) {
                return ResponseEntity.badRequest().body("File type not supported");
            }
            suffix = Optional.of(suffix.get().toLowerCase());
            if (!suffix.get().equalsIgnoreCase("jpg") && !suffix.get().equalsIgnoreCase("jpeg") && !suffix.get().equalsIgnoreCase("png")) {
                return ResponseEntity.badRequest().body("File type not supported");
            }
            String fileName = "user/" + user.getId().toString() + "/" + UUID.randomUUID().toString() + "." + suffix.get();
            minioService.uploadFile(minioService.getImageBucket(), fileName, file);
            return ResponseEntity.ok("/" + minioService.getImageBucket() + "/" + fileName);
        }

        return ResponseEntity.badRequest().body("Usage not supported");
    }


    public int countCommentWeight(UUID commentId) {
        List<Item> items = minioService.getItems(minioService.getCommentBucket(), commentId.toString());
        int weight = 0;
        for (Item item : items) {
            if (item.objectName().endsWith(".jpg") || item.objectName().endsWith(".jpeg") || item.objectName().endsWith(".png")) {
                weight += Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.COMMENT_IMAGE_WEIGHT));
            } else if (item.objectName().endsWith(".mp4")) {
                weight += Integer.parseInt(globalSettingService.getSetting(GlobalSettingService.COMMENT_VIDEO_WEIGHT));
            }
        }
        return weight;
    }

}
