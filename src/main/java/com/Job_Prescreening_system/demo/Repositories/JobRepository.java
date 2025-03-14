package com.Job_Prescreening_system.demo.Repositories;

import com.Job_Prescreening_system.demo.Entities.Job;
import com.Job_Prescreening_system.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByUser(User user);



    // Add this method to find a job by its title
    Job findByTitle(String title);


}
