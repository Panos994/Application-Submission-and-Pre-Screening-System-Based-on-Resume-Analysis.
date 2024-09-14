package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Application;
import com.Job_Prescreening_system.demo.Repositories.ApplicationRepository;
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

}
