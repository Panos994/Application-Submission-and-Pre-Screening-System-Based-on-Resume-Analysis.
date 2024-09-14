package com.Job_Prescreening_system.demo.Entities;


import jakarta.persistence.*;

import java.util.Set;
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String requiredSkills; // Store as a comma-separated string or JSON
    //private int minExperience;
    @Column(name = "min_experience", nullable = true)
    private Integer minExperience;
    private String educationLevel;

    @OneToMany(mappedBy = "job")
    private Set<Application> applications;

    // Getters and setters
    private Double topMatchScore;
    private String topCvFileName;

    private String topCvFileNames;

    // Getters and setters
    public Double getTopMatchScore() {
        return topMatchScore;
    }

    //public String getTopCvFileNames() {
      //  return topCvFileNames;
  //  }

    public String getTopCvFileNames() {
        if (this.topCvFileNames != null && !this.topCvFileNames.isEmpty()) {
            return String.join(", ", this.topCvFileNames);
        }
        return "N/A";
    }





    public void setTopCvFileNames(String topCvFileNames) {
        this.topCvFileNames = topCvFileNames;
    }

    public void setTopMatchScore(Double topMatchScore) {
        this.topMatchScore = topMatchScore;
    }

    public String getTopCvFileName() {
        return topCvFileName;
    }

    public void setTopCvFileName(String topCvFileName) {
        this.topCvFileName = topCvFileName;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }
}
