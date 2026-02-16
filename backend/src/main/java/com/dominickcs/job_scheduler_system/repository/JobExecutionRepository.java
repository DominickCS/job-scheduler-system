package com.dominickcs.job_scheduler_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dominickcs.job_scheduler_system.model.JobExecution;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {

}
