package cn.edu.sustech.ces.service.minio;

import cn.edu.sustech.ces.minio.MinioRegistry;
import io.minio.*;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    private final MinioRegistry minioRegistry;

    private final String READ_ONLY_POLICY = """
            {
                 "Statement": [
                     {
                         "Action": [
                             "s3:GetBucketLocation",
                             "s3:ListBucket"
                         ],
                         "Effect": "Allow",
                         "Principal": "*",
                         "Resource": "arn:aws:s3:::${bucket}"
                     },
                     {
                         "Action": "s3:GetObject",
                         "Effect": "Allow",
                         "Principal": "*",
                         "Resource": "arn:aws:s3:::${bucket}/*"
                     }
                 ],
                 "Version": "2012-10-17"
             }
            """;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        if (!checkBucketExists(minioRegistry.getImageBucket())) {
            createBucket(minioRegistry.getImageBucket());
            setBucketPolicy(minioRegistry.getImageBucket(), READ_ONLY_POLICY.replace("${bucket}", minioRegistry.getImageBucket()));
        }
        if (!checkBucketExists(minioRegistry.getVideoBucket())) {
            createBucket(minioRegistry.getVideoBucket());
            setBucketPolicy(minioRegistry.getVideoBucket(), READ_ONLY_POLICY.replace("${bucket}", minioRegistry.getVideoBucket()));
        }
        if (!checkBucketExists(minioRegistry.getCommentsBucket())) {
            createBucket(minioRegistry.getCommentsBucket());
            setBucketPolicy(minioRegistry.getCommentsBucket(), READ_ONLY_POLICY.replace("${bucket}", minioRegistry.getCommentsBucket()));
        }
        if (!checkBucketExists(minioRegistry.getDocumentBucket())) {
            createBucket(minioRegistry.getDocumentBucket());
            setBucketPolicy(minioRegistry.getDocumentBucket(), READ_ONLY_POLICY.replace("${bucket}", minioRegistry.getDocumentBucket()));
        }
    }

    public String getImageBucket() {
        return minioRegistry.getImageBucket();
    }

    public String getVideoBucket() {
        return minioRegistry.getVideoBucket();
    }

    public String getCommentsBucket() {
        return minioRegistry.getCommentsBucket();
    }

    public String getDocumentBucket() {
        return minioRegistry.getDocumentBucket();
    }

    @SneakyThrows
    public void setBucketPolicy(String bucketName, String policy) {
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy)
                .build());
    }

    @SneakyThrows
    public void createBucket(String bucketName) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public boolean checkBucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public void deleteFile(String bucketName, String fileName) {
        if (!checkBucketExists(bucketName)) {
            throw new RuntimeException("Bucket Not Found");
        }
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    public void deleteDirectory(String bucketName, String directoryName) {
        if (!checkBucketExists(bucketName)) {
            throw new RuntimeException("Bucket Not Found");
        }
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(directoryName)
                .recursive(true)
                .build());
        for (Result<Item> result : results) {
            Item item = result.get();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(item.objectName())
                    .build());
        }
    }

    @SneakyThrows
    public void changeBucketPolicy(String bucketName, String policy) {
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy)
                .build());
    }

    @SneakyThrows
    public void uploadFile(String bucketName, String fileName, MultipartFile file) {
        if (!checkBucketExists(bucketName)) {
            throw new RuntimeException("Bucket Not Found");
        }
        ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());
        if (response == null) {
            throw new RuntimeException("Upload Failed");
        }
    }

    @SneakyThrows
    public List<Item> getItems(String commentsBucket, String prefix) {
        if (!checkBucketExists(commentsBucket)) {
            throw new RuntimeException("Bucket Not Found");
        }
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(commentsBucket)
                .prefix(prefix)
                .recursive(true)
                .build());
        List<Item> items = new ArrayList<>();
        for (Result<Item> result : results) {
            items.add(result.get());
        }
        return items;
    }
}
