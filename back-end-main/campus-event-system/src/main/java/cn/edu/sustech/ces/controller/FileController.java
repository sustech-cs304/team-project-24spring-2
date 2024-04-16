package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.service.minio.MinioService;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileController {

    private final MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String bucketName, @RequestBody MultipartFile file) {
        String fileName = UUID.randomUUID() + "+" + file.getOriginalFilename();
        JSONObject response = new JSONObject();
        try {
            minioService.uploadFile(bucketName, fileName, file);
        } catch (Exception e) {
            response.put("message", "Upload Failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response.toJSONString());
        }
        response.put("bucket_name", bucketName);
        response.put("file_name", fileName);
        response.put("message", "Upload Success");
        return ResponseEntity.ok(response.toJSONString());
    }



}
