package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Job_Prescreening_system.demo.Services.JobRequirements;
@Service
public class ScoringService {

    public int calculateMatchScore(String parsedResume, JobRequirements jobRequirements) {
        int score = 0;

        // Check for skills match
        for (String skill : jobRequirements.getRequiredSkills()) {
            if (parsedResume.toLowerCase().contains(skill.toLowerCase())) {
                score += 10; // +10 points for each matched skill
            }
        }

        // Check for experience
        if (parsedResume.contains(jobRequirements.getMinExperience() + " years")) {
            score += 20;
        }

        // Check for education level
        if (parsedResume.toLowerCase().contains(jobRequirements.getEducationLevel().toLowerCase())) {
            score += 15;
        }

        return score;
    }

}
