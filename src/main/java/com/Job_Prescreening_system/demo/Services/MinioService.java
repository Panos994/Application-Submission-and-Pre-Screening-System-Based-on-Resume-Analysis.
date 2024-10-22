package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.REST.ApplicationController;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioService {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void initializeMinioClient() {
        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();


    }

    public String uploadFile(MultipartFile file) throws Exception {
        String objectName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }
        return minioUrl + "/" + bucketName + "/" + objectName;
    }








    //-----
    private static final Logger log = LoggerFactory.getLogger(MinioService.class);
    public InputStream getFile(String fileName) throws Exception {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (MinioException e) {
            log.error("MinIO error while getting file: {}", e.getMessage());
            throw new Exception("Error fetching file from MinIO", e);
        } catch (Exception e) {
            log.error("Error fetching file: {}", e.getMessage());
            throw e; // Rethrow to catch in the controller
        }
    }
}
