package com.Job_Prescreening_system.demo.REST;

import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Services.GPTService;
import com.Job_Prescreening_system.demo.Services.JobService;
import com.Job_Prescreening_system.demo.Services.StripeService;
import com.stripe.exception.StripeException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.tika.Tika;
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





    // without stripe subscription trial

    @Secured("ROLE_USER")
    @PostMapping("/match-candidate")
    public ResponseEntity<String> matchCandidate(@RequestParam("resume") MultipartFile resumeFile) {
        try (PDDocument document = PDDocument.load(resumeFile.getInputStream())) {
            // Extract text from resume using PDFBox
            String resumeText = new PDFTextStripper().getText(document);

            // Get list of jobs from the job service
            List<Job> jobList = jobService.getAllJobs();

            // Analyze the resume text using GPT to find the top matches
            String bestMatches = gptService.analyzeResumeAndMatchJobs(resumeText, jobList);

            return ResponseEntity.ok(bestMatches);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the resume file.");
        }
    }







    // subscription with stripe trial
/*
    @Autowired
    private StripeService stripeService;

    @Secured("ROLE_USER")
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeUser(@RequestParam("customerId") String customerId,
                                                @RequestParam("plan") String plan) {
        try {
            String priceId;
            switch (plan.toLowerCase()) {
                case "monthly":
                    priceId = "price_monthly_plan"; // Replace with actual price ID
                    break;
                case "6month":
                    priceId = "price_6month_plan"; // Replace with actual price ID
                    break;
                case "yearly":
                    priceId = "price_yearly_plan"; // Replace with actual price ID
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid subscription plan.");
            }

            String subscriptionId = stripeService.createSubscription(customerId, priceId);
            return ResponseEntity.ok("Subscription created: " + subscriptionId);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating subscription: " + e.getMessage());
        }
    }





    @Secured("ROLE_USER")
    @PostMapping("/match-candidate")
    public ResponseEntity<String> matchCandidate(@RequestParam("resume") MultipartFile resumeFile,
                                                 @RequestParam("customerId") String customerId) {
        try {
            if (!stripeService.isUserSubscribed(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not subscribed.");
            }

            try (PDDocument document = PDDocument.load(resumeFile.getInputStream())) {
                String resumeText = new PDFTextStripper().getText(document);
                List<Job> jobList = jobService.getAllJobs();
                String bestMatches = gptService.analyzeResumeAndMatchJobs(resumeText, jobList);
                return ResponseEntity.ok(bestMatches);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the resume.");
        }
    }



*/



}