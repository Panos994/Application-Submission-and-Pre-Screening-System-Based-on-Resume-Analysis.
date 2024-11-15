package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.ApplicationRepository;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    // Other CRUD operations



    public Application getTopApplicationByJobId(Long jobId) {
        List<Application> applications = applicationRepository.findTopApplicationsByJobId(jobId);
        return applications.isEmpty() ? null : applications.get(0);
    }
    public List<Application> getTopApplicationsByJobId(Long jobId) {
        List<Application> applications = applicationRepository.findByJobId(jobId);
        if (applications.isEmpty()) {
            return new ArrayList<>();
        }

        // Find the highest score (if matchScore is a Double, use mapToDouble)
        double highestScore = applications.stream()
                .mapToDouble(Application::getMatchScore)
                .max()
                .orElse(0.0);

        // Return all applications that have the highest score (if matchScore is a Double, compare as double)
        return applications.stream()
                .filter(application -> application.getMatchScore() == highestScore)
                .collect(Collectors.toList());
    }









    //-----------------

    public Application findById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }










    //---------
    public Application updateApplicationStatus(Long id, String newStatus, Integer progress) {
        Application application = applicationRepository.findById(id).orElse(null);
        if (application != null) {
            application.setStatus(newStatus);
            application.setProgress(progress);
            Application savedApplication = applicationRepository.save(application);
            System.out.println("Updated Application: " + savedApplication); // Debugging
            return savedApplication;
        }
        return null;
    }





    @Autowired
    private UserRepository userRepository;

    public List<Application> getApplicationsByCandidate(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findByUserId(user.getId());
    }

}
