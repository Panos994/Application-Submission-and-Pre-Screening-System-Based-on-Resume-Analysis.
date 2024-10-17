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

    // Criteria weights
    @Column(nullable = false)
    private Double minExperienceWeight;

    @Column(nullable = false)
    private Double educationWeight;

    @Column(nullable = false)
    private Double skillsWeight;


    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    public Double getMinExperienceWeight() {
        return minExperienceWeight;
    }

    public void setMinExperienceWeight(Double minExperienceWeight) {
        this.minExperienceWeight = minExperienceWeight;
    }

    public Double getEducationWeight() {
        return educationWeight;
    }

    public void setEducationWeight(Double educationWeight) {
        this.educationWeight = educationWeight;
    }

    public Double getSkillsWeight() {
        return skillsWeight;
    }

    public void setSkillsWeight(Double skillsWeight) {
        this.skillsWeight = skillsWeight;
    }

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


    private String institutionType;

    private String universityPreference;


    // Getters and setters for new fields
    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }


    // Add getter and setter for universityPreference
    public String getUniversityPreference() {
        return universityPreference;
    }

    public void setUniversityPreference(String universityPreference) {
        this.universityPreference = universityPreference;
    }



    private String location; // e.g., Athens, Greece
    private String sector; // e.g., Informatics, Marketing
    private String workType; // e.g., full time, part time, remote
    private String jobLevel; //

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", nullable = false) // Link the job to the user who created it
    private User user;

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}