package cn.edu.sustech.ces.service.minio;

import cn.edu.sustech.ces.minio.MinioRegistry;
import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        if (!checkBucketExists(minioRegistry.getDocumentBucket())) {
            createBucket(minioRegistry.getDocumentBucket());
        }
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
    public void removeFile(String bucketName, String fileName) {
        if (!checkBucketExists(bucketName)) {
            throw new Exception("Bucket Not Found");
        }
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
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
            throw new Exception("Bucket Not Found");
        }
        ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());
        if (response == null) {
            throw new Exception("Upload Failed");
        }
    }

}
