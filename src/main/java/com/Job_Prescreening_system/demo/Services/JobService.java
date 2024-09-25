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

            if (!topApplications.isEmpty()) {
                double topScore = topApplications.get(0).getMatchScore();
                job.setTopMatchScore(topScore);

                List<String> topCvFileNames = topApplications.stream()
                        .map(Application::getCvFileName)
                        .collect(Collectors.toList());
                job.setTopCvFileNames(String.join(", ", topCvFileNames));
            }
        }

        return jobs;
    }

    // Save a new job with weight validation
    public Job saveJob(Job job) {
        double totalWeight = job.getMinExperienceWeight() + job.getEducationWeight() + job.getSkillsWeight();

        if (Math.abs(totalWeight - 100.0) > 0.0001) { // Allowing a tiny margin for floating-point arithmetic
            throw new RuntimeException("Total weight must equal 100%.");
        }

        return jobRepository.save(job);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Update an existing job with weight validation
    public Job updateJob(Long id, Job job) {
        Optional<Job> existingJobOpt = jobRepository.findById(id);
        if (existingJobOpt.isPresent()) {
            Job existingJob = existingJobOpt.get();
            existingJob.setTitle(job.getTitle());
            existingJob.setDescription(job.getDescription());
            existingJob.setRequiredSkills(job.getRequiredSkills());
            existingJob.setMinExperience(job.getMinExperience());
            existingJob.setEducationLevel(job.getEducationLevel());
            existingJob.setMinExperienceWeight(job.getMinExperienceWeight());
            existingJob.setEducationWeight(job.getEducationWeight());
            existingJob.setSkillsWeight(job.getSkillsWeight());

            double totalWeight = existingJob.getMinExperienceWeight() + existingJob.getEducationWeight() + existingJob.getSkillsWeight();
            if (Math.abs(totalWeight - 100.0) > 0.0001) {
                throw new RuntimeException("Total weight must equal 100%.");
            }

            return jobRepository.save(existingJob);
        } else {
            throw new RuntimeException("Job with ID " + id + " not found.");
        }
    }

    // Get job by ID
    public Job getJobById(Long id) {
        Optional<Job> jobOpt = jobRepository.findById(id);
        if (jobOpt.isPresent()) {
            return jobOpt.get();
        } else {
            throw new RuntimeException("Job with ID " + id + " not found.");
        }
    }
}
