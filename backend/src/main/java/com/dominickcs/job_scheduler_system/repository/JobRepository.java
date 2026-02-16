package com.dominickcs.job_scheduler_system.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobStatus;

public interface JobRepository extends JpaRepository<Job, Long> {
  @Query("SELECT j FROM Job j WHERE j.nextExecutionTime <= :currentTime AND j.jobStatus = :jobStatus AND j.enabled = true")
  List<Job> findJobsDueForExecution(LocalDateTime currentTime, JobStatus jobStatus);

}
