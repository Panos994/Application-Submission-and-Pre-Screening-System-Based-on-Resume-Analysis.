package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.JobRepository;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
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



    @Autowired
    private UserRepository userRepository;


    // Save a new job with weight validation
    public Job saveJob(Job job, String username) {
        double totalWeight = job.getMinExperienceWeight() + job.getEducationWeight() + job.getSkillsWeight();

        if (Math.abs(totalWeight - 100.0) > 0.0001) {
            throw new RuntimeException("Total weight must equal 100%.");
        }

        // Find the user by their username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // Retrieve the user from the Optional
        User user = userOptional.get();

        // Set the user who created the job
        job.setUser(user);

        return jobRepository.save(job);
    }






    // Get all jobs
    public List<Job> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        // Add logging here
        System.out.println("Fetched jobs: " + jobs);
        return jobs;
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



    public List<Job> getJobsByUsername(String username) {
        // Find the user by their username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // Retrieve the user from the Optional
        User user = userOptional.get();

        // Return the list of jobs by the user
        return jobRepository.findByUser(user);
    }





    public void updateJobWithTopApplication(Job job) {
        List<Application> topApplications = applicationService.getTopApplicationsByJobId(job.getId());

        if (!topApplications.isEmpty()) {
            double topScore = topApplications.get(0).getMatchScore();
            job.setTopMatchScore(topScore);

            List<String> topCvFileNames = topApplications.stream()
                    .map(Application::getCvFileName)
                    .collect(Collectors.toList());
            job.setTopCvFileNames(String.join(", ", topCvFileNames));
        }

        jobRepository.save(job); // Save the job with updated score and CV file names
    }

}
