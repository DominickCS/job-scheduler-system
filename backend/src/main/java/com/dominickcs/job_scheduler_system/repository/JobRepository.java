package com.dominickcs.job_scheduler_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dominickcs.job_scheduler_system.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
  @Query("select job from jobs where job.nextExecutionTime <= NOW() AND jobStatus = SCHEDULED AND enabled = true")
  List<Job> findJobsDueForExecution();

}
