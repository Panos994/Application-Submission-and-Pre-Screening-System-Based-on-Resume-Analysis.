package com.Job_Prescreening_system.demo.Services;


import jakarta.persistence.Column;

import java.util.List;

public class JobRequirements {
    private List<String> requiredSkills;
    private int minExperience; // In years
    private String educationLevel;




    // Constructor, getters, setters
    public JobRequirements(List<String> requiredSkills, int minExperience, String educationLevel) {
        this.requiredSkills = requiredSkills;
        this.minExperience = minExperience;
        this.educationLevel = educationLevel;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public String getEducationLevel() {
        return educationLevel;
    }




}