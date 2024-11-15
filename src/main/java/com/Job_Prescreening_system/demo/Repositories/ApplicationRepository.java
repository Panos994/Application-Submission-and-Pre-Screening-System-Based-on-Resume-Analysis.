package com.Job_Prescreening_system.demo.Repositories;

import com.Job_Prescreening_system.demo.Entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // Custom query methods (if any)

    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId ORDER BY a.matchScore DESC")
    List<Application> findTopApplicationsByJobId(Long jobId);


    // Define a method to find applications by jobId
    List<Application> findByJobId(Long jobId);






    List<Application> findByUserId(Long userId);

}
