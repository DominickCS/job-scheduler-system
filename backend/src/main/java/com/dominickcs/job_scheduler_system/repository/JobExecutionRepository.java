package com.dominickcs.job_scheduler_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dominickcs.job_scheduler_system.model.JobExecution;

import io.lettuce.core.dynamic.annotation.Param;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
  @Query("SELECT je FROM JobExecution je WHERE je.job.id = :jobId ORDER BY je.startTime DESC")
  List<JobExecution> findByJobIdOrderByStartTimeDesc(@Param("jobId") Long jobId);

  List<JobExecution> findTop10ByOrderByIdDesc();
}
