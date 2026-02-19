package com.dominickcs.job_scheduler_system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
// import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import com.dominickcs.job_scheduler_system.executor.JobExecutorRegistry;
import com.dominickcs.job_scheduler_system.executor.JobExecutorResult;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobExecution;
import com.dominickcs.job_scheduler_system.model.JobExecutionStatus;
import com.dominickcs.job_scheduler_system.model.JobStatus;
import com.dominickcs.job_scheduler_system.repository.JobExecutionRepository;
import com.dominickcs.job_scheduler_system.repository.JobRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
  private final JobRepository jobRepository;
  private final JobExecutorRegistry jExecutorRegistry;
  private final JobExecutionRepository jExecutionRepository;

  public ScheduleService(JobRepository jobRepository, JobExecutorRegistry jExecutorRegistry,
      JobExecutionRepository jExecutionRepository) {
    this.jobRepository = jobRepository;
    this.jExecutorRegistry = jExecutorRegistry;
    this.jExecutionRepository = jExecutionRepository;
  }

  @Scheduled(fixedDelay = 5000)
  @Transactional
  public void processJobs() {
    List<Job> jobsToExecute = jobRepository.findJobsDueForExecution(LocalDateTime.now(), JobStatus.SCHEDULED);
    System.out.println("Found " + jobsToExecute.size() + " jobs to execute");
    System.out.println();

    for (Job job : jobsToExecute) {
      System.out.println();
      System.out.println("Executing job: " + job.getJobName());
      System.out.println("------------------------------------");
      executeJob(job);
    }
  }

  public void executeJob(Job job) {
    JobExecution jobExecution = new JobExecution();
    jobExecution.setJob(job);
    jobExecution.setStartTime(LocalDateTime.now());
    job.setJobStatus(JobStatus.RUNNING);
    job.setLastExecution(LocalDateTime.now());
    jobRepository.save(job);
    JobExecutorResult result = jExecutorRegistry.getExecutor(job.getJobType()).execute(job);

    if (result.isJobSuccessful()) {
      jobExecution.setStatus(JobExecutionStatus.SUCCESS);
      job.setJobStatus(JobStatus.COMPLETED);
      job.setSuccessCounter(job.getSuccessCounter() + 1);
      LocalDateTime nextRun = calculateNextExecutionTime(job);
      if (nextRun != null) {
        job.setNextExecution(nextRun);
        job.setJobStatus(JobStatus.SCHEDULED);
      } else {
        job.setJobStatus(JobStatus.COMPLETED);
        job.setNextExecution(null);
      }
      jobExecution.setEndTime(LocalDateTime.now());
      jobExecution.setDurationMs(result.getExecutionTimeMs());
      jobExecution.setMessage(result.getMessage());
      jExecutionRepository.save(jobExecution);
      jobRepository.save(job);
      System.out.println("Job: " + job.getJobName() + " ran successfully!");
      System.out.println();
    } else {
      jobExecution.setEndTime(LocalDateTime.now());
      jobExecution.setDurationMs(result.getExecutionTimeMs());
      jobExecution.setStatus(JobExecutionStatus.FAILURE);
      jobExecution.setErrorMessage(result.getErrorMessage());
      job.setJobStatus(JobStatus.FAILED);
      job.setLastErrorMessage(result.getMessage());
      job.setFailureCounter(job.getFailureCounter() + 1);
      jExecutionRepository.save(jobExecution);
      jobRepository.save(job);
      System.out.println("Job: " + job.getJobName() + " failed to run.");
      System.out.println();
    }

  }

  private LocalDateTime calculateNextExecutionTime(Job job) {
    switch (job.getScheduleType()) {
      case CRON:
        return calculateNextCronTime(job.getCronExpression());

      case FIXED_DELAY:
        return LocalDateTime.now().plus(
            java.time.Duration.ofMillis(job.getFixedDelay()));

      case ONE_TIME:
        return null;

      default:
        throw new IllegalArgumentException("Unknown schedule type: " + job.getScheduleType());
    }
  }

  private LocalDateTime calculateNextCronTime(String cronExpressionStr) {
    CronExpression cronExpression = CronExpression.parse(cronExpressionStr);
    return cronExpression.next(LocalDateTime.now());
  }
}
