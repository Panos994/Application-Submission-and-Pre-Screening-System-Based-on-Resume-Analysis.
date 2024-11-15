package com.Job_Prescreening_system.demo.REST;
import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public ResponseEntity<?> downloadResume(@PathVariable Long id) {
        log.info("Attempting to download resume for application ID: {}", id);

        Application application = applicationService.findById(id);
        if (application == null || application.getCvFileName() == null) {
            log.warn("Application not found or no CV for ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        // Extract the object key from the URL stored in the application
        String url = application.getCvFileName();
        String objectName = url.substring(url.lastIndexOf("/") + 1);

        log.info("Fetching file from MinIO: {}", objectName);

        try {
            // Attempt to retrieve the file directly
            InputStream inputStream = minioService.getFile(objectName);
            log.info("Successfully retrieved file: {}", objectName);

            // Stream the file directly if successful
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            log.error("Error streaming file; falling back to presigned URL: {}", objectName, e);

            try {
                // Fallback: Generate a presigned URL if direct streaming fails
                String presignedUrl = minioService.generatePresignedUrl(objectName);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=\"" + objectName + "\"");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(presignedUrl);

            } catch (Exception ex) {
                log.error("Failed to generate presigned URL: {}", objectName, ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating download link");
            }
        }
    }






//-----------

    @PutMapping("/{id}/viewed")
    public ResponseEntity<?> markAsViewed(@PathVariable Long id) {
        Application updatedApplication = applicationService.updateApplicationStatus(id, "VIEWED", 2);
        if (updatedApplication == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
        }
        return ResponseEntity.ok(updatedApplication);
    }

    // You can also include a generic endpoint to update any status:
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Application updatedApplication = applicationService.updateApplicationStatus(id, status, null); // progress is optional
        if (updatedApplication == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
        }
        return ResponseEntity.ok(updatedApplication);
    }














    @Autowired
    private UserRepository userRepository;


    @GetMapping("/user")
    public ResponseEntity<List<Application>> getApplicationsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Ensure the user is authenticated
        if (userDetails == null || userDetails.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Fetch the user's applications
        List<Application> applications = applicationService.getApplicationsByCandidate(userDetails.getUsername());

        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(applications);
    }







}
