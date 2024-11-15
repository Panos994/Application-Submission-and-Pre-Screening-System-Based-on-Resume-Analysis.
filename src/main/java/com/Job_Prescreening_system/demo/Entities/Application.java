package com.Job_Prescreening_system.demo.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
@Entity
@Table(name = "applications")
public class Application {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonManagedReference  // Ignore job to break potential circular reference
    private Job job;

    private Double matchScore;  // Automated score based on resume parsing and job requirements
    private String status ="PENDING";      // e.g. "pending", "reviewed", "accepted"
    private String cvFileName;  // Name of the CV file


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCvFileName() {
        return cvFileName;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }


    private Integer progress;

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usrid", nullable = false)
    @JsonBackReference
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

