package com.Job_Prescreening_system.demo.REST;

import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:8080")
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


    @PostMapping("/apply")
    public ResponseEntity<String> applyForJob(@RequestParam("resume") MultipartFile resumeFile, @RequestParam("jobId") Long jobId) {
        try {
            // Parse the resume
            String parsedResume = resumeParsingService.parseResume(resumeFile);

            // Get the job by its ID
            Job job = jobService.getJobById(jobId);
            JobRequirements jobRequirements = new JobRequirements(
                    List.of(job.getRequiredSkills().split(",")),
                    job.getMinExperience(),
                    job.getEducationLevel()
            );

            // Calculate match score
            double matchScore = scoringService.calculateMatchScore(parsedResume, jobRequirements);

            // Save the application with the score
            Application application = new Application();
            application.setJob(job);
            application.setMatchScore(matchScore);
            application.setCvFileName(resumeFile.getOriginalFilename());
            applicationService.saveApplication(application);

            // Return the score and success message
            return ResponseEntity.ok("Your resume scored: " + matchScore + " points!");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to parse resume: " + e.getMessage());
        }
    }


    // Endpoint to get a specific job by ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        try {
            Job job = jobService.getJobById(id);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }













    // Create a new job (only accessible to authenticated users, maybe only ADMIN)
    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody Job job) {
        try {
            Job savedJob = jobService.saveJob(job);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating job: " + e.getMessage());
        }
    }

    // Fetch all jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(jobs);
    }


    @GetMapping("/topApplication")
    public ResponseEntity<List<Job>> getAllTopJobs() {
        List<Job> jobs = jobService.getAllJobsWithTopApplications();
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(jobs);
    }

    // Update job by ID (only accessible to authenticated users, maybe only ADMIN)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job job) {
        try {
            Job updatedJob = jobService.updateJob(id, job);
            if (updatedJob != null) {
                return ResponseEntity.ok(updatedJob);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job with ID " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating job: " + e.getMessage());
        }
    }










    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/export")
    public void exportJobsToExcel(HttpServletResponse response) throws IOException {
        List<Job> jobs = jobService.getAllJobsWithTopApplications();  // Or any other method to fetch jobs
        excelExportService.exportJobListToExcel(jobs, response);
    }
}
