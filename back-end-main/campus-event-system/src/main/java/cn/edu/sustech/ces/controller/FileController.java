package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Comment;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.service.CommentService;
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
            minioService.uploadFile(minioService.getCommentsBucket(), fileName, file);
            return ResponseEntity.ok(fileName);
        }
        return ResponseEntity.badRequest().body("Usage not supported");
    }


    public int countCommentWeight(UUID commentId) {
        List<Item> items = minioService.getItems(minioService.getCommentsBucket(), commentId.toString());
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
