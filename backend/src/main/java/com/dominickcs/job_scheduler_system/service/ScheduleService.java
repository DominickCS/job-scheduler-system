package com.dominickcs.job_scheduler_system.service;

import java.time.LocalDateTime;
import java.util.List;

// import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import com.dominickcs.job_scheduler_system.executor.JobExecutorRegistry;
import com.dominickcs.job_scheduler_system.executor.JobExecutorResult;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobStatus;
import com.dominickcs.job_scheduler_system.repository.JobRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
  private JobRepository jobRepository;
  private JobExecutorRegistry jExecutorRegistry;
  // private CronExpression cronExpression;

  public ScheduleService(JobRepository jobRepository, JobExecutorRegistry jExecutorRegistry) {
    this.jobRepository = jobRepository;
    this.jExecutorRegistry = jExecutorRegistry;
    // this.cronExpression = cronExpression;
  }

  @Transactional
  public void processJobs() {
    List<Job> jobsToExecute = jobRepository.findJobsDueForExecution(LocalDateTime.now(), JobStatus.SCHEDULED);

    for (Job job : jobsToExecute) {
      job.setJobStatus(JobStatus.RUNNING);
      executeJob(job);
    }
  }

  public void executeJob(Job job) {
    JobExecutorResult result = jExecutorRegistry.getExecutor(job.getJobType()).execute(job);

    if (result.isJobSuccessful()) {
      job.setJobStatus(JobStatus.COMPLETED);
      job.setSuccessCounter(job.getSuccessCounter() + 1);
      jobRepository.save(job);
      System.out.println("Job: " + job.getJobName() + " ran successfully!");
    } else {
      job.setJobStatus(JobStatus.FAILED);
      job.setLastErrorMessage(result.getMessage());
      job.setFailureCounter(job.getFailureCounter() + 1);
      jobRepository.save(job);
      System.out.println("Job: " + job.getJobName() + " failed to run.");
    }

  }
}
