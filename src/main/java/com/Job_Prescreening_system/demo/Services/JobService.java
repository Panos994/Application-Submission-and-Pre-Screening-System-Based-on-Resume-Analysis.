package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;





    @Autowired
    private ApplicationService applicationService;

    // Get all jobs with their highest score applications
    public List<Job> getAllJobsWithTopApplications() {
        List<Job> jobs = jobRepository.findAll();

        for (Job job : jobs) {
            List<Application> topApplications = applicationService.getTopApplicationsByJobId(job.getId());

            // Get the top score from the list of top applications
            if (!topApplications.isEmpty()) {
                double topScore = topApplications.get(0).getMatchScore();
                job.setTopMatchScore(topScore);

                // Combine all CV file names with the top score
                List<String> topCvFileNames = topApplications.stream()
                        .map(Application::getCvFileName)
                        .collect(Collectors.toList());
                job.setTopCvFileNames(String.join(", ", topCvFileNames));
            }
        }

        return jobs;
    }


    // Save a new job
    public Job saveJob(Job job) {
        try {
            return jobRepository.save(job);
        } catch (Exception e) {
            throw new RuntimeException("Error saving job: " + e.getMessage());
        }
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Update an existing job
    public Job updateJob(Long id, Job job) {
        Optional<Job> existingJob = jobRepository.findById(id);
        if (existingJob.isPresent()) {
            Job updatedJob = existingJob.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setDescription(job.getDescription());
            updatedJob.setRequiredSkills(job.getRequiredSkills());
            updatedJob.setMinExperience(job.getMinExperience());
            updatedJob.setEducationLevel(job.getEducationLevel());
            return jobRepository.save(updatedJob);
        } else {
            throw new RuntimeException("Job with ID " + id + " not found.");
        }
    }







    public Job getJobById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            return job.get();
        } else {
            throw new RuntimeException("Job with ID " + id + " not found.");
        }
    }

}