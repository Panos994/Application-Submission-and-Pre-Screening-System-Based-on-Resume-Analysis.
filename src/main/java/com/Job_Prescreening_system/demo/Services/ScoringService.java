package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Job;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScoringService {

    public double calculateMatchScore(String parsedResume, JobRequirements jobRequirements, Job job) {
        double experienceScore = calculateExperienceScore(parsedResume, jobRequirements.getMinExperience(), job.getMinExperienceWeight());
        double educationScore = calculateEducationScore(parsedResume, jobRequirements.getEducationLevel(), job.getEducationWeight());
        double skillsScore = calculateSkillsScore(parsedResume, jobRequirements.getRequiredSkills(), job.getSkillsWeight());

        return experienceScore + educationScore + skillsScore;
    }

    private double calculateExperienceScore(String parsedResume, int minExperience, double weight) {
        boolean matches = parsedResume.contains(minExperience + " years");
        return matches ? weight : 0; // Full weight if matched
    }

    private double calculateEducationScore(String parsedResume, String educationLevel, double weight) {
        boolean matches = parsedResume.toLowerCase().contains(educationLevel.toLowerCase());
        return matches ? weight : 0; // Full weight if matched
    }

    private double calculateSkillsScore(String parsedResume, List<String> requiredSkills, double weight) {
        double matchedSkills = 0;
        for (String skill : requiredSkills) {
            if (parsedResume.toLowerCase().contains(skill.toLowerCase())) {
                matchedSkills += 1;
            }
        }
        if (requiredSkills.isEmpty()) {
            return 0;
        }
        double skillScore = (matchedSkills / requiredSkills.size()) * weight;
        return skillScore;
    }
}
