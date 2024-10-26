
package com.Job_Prescreening_system.demo.REST;

import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Services.GPTService;
import com.Job_Prescreening_system.demo.Services.JobService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class JobMatchingControllerGPT {

    @Autowired
    private GPTService gptService;

    @Autowired
    private JobService jobService;

    @Secured("ROLE_USER")
    @PostMapping("/match-candidate")
    public ResponseEntity<String> matchCandidate(@RequestParam("resume") MultipartFile resumeFile) {
        try {
            // Extract text from resume using Apache Tika
            String resumeText = new Tika().parseToString(resumeFile.getInputStream());

            // Get list of jobs from the job service
            List<Job> jobList = jobService.getAllJobs();

            // Analyze the resume text using GPT to find the top 3 job matches
            String bestMatches = gptService.analyzeResumeAndMatchJobs(resumeText, jobList);

            return ResponseEntity.ok(bestMatches);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the resume file.");
        }
    }



}


