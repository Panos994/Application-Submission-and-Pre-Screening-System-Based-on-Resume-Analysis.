package com.Job_Prescreening_system.demo.Services;


import com.Job_Prescreening_system.demo.Services.JobRequirements;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class JobRequirementsService {
        //for testing purposes
    public JobRequirements getJobRequirements() {
        return new JobRequirements(
                Arrays.asList("Java", "Spring Boot", "Microservices"), // Skills required for the job
                3, // Minimum 3 years of experience required
                "Bachelor's Degree" // Education level required
        );
    }
}
