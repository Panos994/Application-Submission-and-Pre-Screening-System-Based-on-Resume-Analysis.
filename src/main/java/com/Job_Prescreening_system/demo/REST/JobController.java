package com.Job_Prescreening_system.demo.REST;

import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
import com.Job_Prescreening_system.demo.Services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:8081")
public class JobController {

    @Autowired
    private ResumeParsingService resumeParsingService;

    @Autowired
    private JobRequirementsService jobRequirementsService;

    @Autowired
    private ScoringService scoringService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobService jobService;


    @Autowired
    private MinioService minioService;



    @Autowired
    private UserRepository userRepository;


    // Apply for a job
    @Secured("ROLE_USER")
    @PostMapping("/apply")
    public ResponseEntity<String> applyForJob(
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestParam("jobId") Long jobId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Fetch the user from the username in the userDetails object
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Parse the resume
            String parsedResume = resumeParsingService.parseResume(resumeFile);

            // Upload the file to MinIO
            String minioUrl = minioService.uploadFile(resumeFile);

            // Get the job by its ID
            Job job = jobService.getJobById(jobId);
            if (job == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
            }

            JobRequirements jobRequirements = new JobRequirements(
                    List.of(job.getRequiredSkills().split(",")),
                    job.getMinExperience(),
                    job.getEducationLevel()
            );

            // Calculate match score
            double matchScore = scoringService.calculateMatchScore(parsedResume, jobRequirements, job);

            // Save the application with the score and MinIO file URL
            Application application = new Application();
            application.setJob(job);
            application.setMatchScore(matchScore);
            application.setCvFileName(minioUrl); // Store the URL from MinIO
            application.setUser(user); // Set the user for this application
            applicationService.saveApplication(application);

            // Update job with the new top application
            jobService.updateJobWithTopApplication(job);

            // Return the score and success message
            return ResponseEntity.ok("Your resume scored: " + matchScore + " points!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to submit application: " + e.getMessage());
        }
    }




    // Get a specific job by ID
    //@Secured("ROLE_MODERATOR,ROLE_USER")
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        try {
            Job job = jobService.getJobById(id);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Create a new job (only accessible to employee)
    @Secured("ROLE_MODERATOR")
    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody Job job, Principal principal) {
        try {
            // Get the logged-in user's username from the Principal object
            String username = principal.getName();

            Job savedJob = jobService.saveJob(job, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating job: " + e.getMessage());
        }
    }

    // Fetch all jobs



    // Fetch all jobs with top applications
    @Secured("ROLE_USER")
    @GetMapping("/topApplication")
    public ResponseEntity<List<Job>> getAllTopJobs() {
        List<Job> jobs = jobService.getAllJobsWithTopApplications();
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(jobs);
    }

    // Update job by ID (only accessible to ADMIN)
    @Secured("ROLE_MODERATOR")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job job) {
        try {
            Job updatedJob = jobService.updateJob(id, job);
            return ResponseEntity.ok(updatedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating job: " + e.getMessage());
        }
    }

    // Export jobs to Excel
    @Autowired
    private ExcelExportService excelExportService;
    //@Secured("ROLE_MODERATOR")
    @GetMapping("/export")
    public void exportJobsToExcel(HttpServletResponse response) throws IOException {
        List<Job> jobs = jobService.getAllJobsWithTopApplications();  // Or any other method to fetch jobs
        excelExportService.exportJobListToExcel(jobs, response);
    }



    @Secured("ROLE_MODERATOR")
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Job>> getJobsByUser(@PathVariable String username) {
        List<Job> jobs = jobService.getJobsByUsername(username);
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(jobs);
    }







 /*
    @GetMapping("/allJob")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(jobs);
    }
       */


    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobsPublic() {
        List<Job> jobs = jobService.getAllJobs();
        if (jobs == null || jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(jobs);
    }









}
