package com.Job_Prescreening_system.demo.REST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Services.ApplicationService;
import com.Job_Prescreening_system.demo.Services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PostMapping
    public Application createApplication(@RequestBody Application application) {
        return applicationService.saveApplication(application);
    }















//-------
    @Autowired
    private MinioService minioService;
    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);
    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadResume(@PathVariable Long id) {
        log.info("Attempting to download resume for application ID: {}", id);

        Application application = applicationService.findById(id);
        if (application == null || application.getCvFileName() == null) {
            log.warn("Application not found or no CV for ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        // Extract the object key from the URL
        String url = application.getCvFileName();
        String objectName = url.substring(url.lastIndexOf("/") + 1); // This will get the object key

        log.info("Fetching file from MinIO: {}", objectName);

        try {
            InputStream inputStream = minioService.getFile(objectName);
            log.info("Successfully retrieved file: {}", objectName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf") // or application/octet-stream if preferred
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            log.error("I/O error while downloading file: {}", objectName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            log.error("Unexpected error while downloading file: {}", objectName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }






}
