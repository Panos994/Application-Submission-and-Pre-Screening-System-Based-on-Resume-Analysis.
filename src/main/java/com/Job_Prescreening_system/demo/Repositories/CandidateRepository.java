package com.Job_Prescreening_system.demo.Repositories;

import com.Job_Prescreening_system.demo.Entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
