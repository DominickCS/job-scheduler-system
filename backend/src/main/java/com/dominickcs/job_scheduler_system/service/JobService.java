package com.dominickcs.job_scheduler_system.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequestDTO;
import com.dominickcs.job_scheduler_system.dto.JobResponseDTO;
import com.dominickcs.job_scheduler_system.dto.UpdateJobRequestDTO;
import com.dominickcs.job_scheduler_system.exception.JobNotFoundException;
import com.dominickcs.job_scheduler_system.mapper.JobMapper;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobStatus;
import com.dominickcs.job_scheduler_system.repository.JobExecutionRepository;
import com.dominickcs.job_scheduler_system.repository.JobRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class JobService {
  private final JobRepository jobRepository;
  private final JobExecutionRepository jExecutionRepository;

  public JobService(JobRepository jobRepository, JobExecutionRepository jExecutionRepository) {
    this.jobRepository = jobRepository;
    this.jExecutionRepository = jExecutionRepository;
  }

  public JobResponseDTO createJob(CreateJobRequestDTO request) {
    validateScheduleRequirements(request);
    Job job = JobMapper.toEntity(request);

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setSuccessCounter(0L);
    job.setFailureCounter(0L);

    LocalDateTime nextExecution = calculateInitialExecutionTime(job);
    job.setNextExecutionTime(nextExecution);

    Job savedJob = jobRepository.save(job);

    return JobMapper.jobResponseDTO(savedJob);
  }

  public JobResponseDTO updateJob(Long id, UpdateJobRequestDTO request) {
    Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

    if (request.getJobName() != null) {
      job.setJobName(request.getJobName());
    }

    if (request.getCronExpression() != null) {
      validateCronExpression(request.getCronExpression());
      job.setCronExpression(request.getCronExpression());
    }

    if (request.getJobDescription() != null) {
      job.setJobDescription(request.getJobDescription());
    }

    if (request.getNextExecutionTime() != null) {
      job.setNextExecutionTime(request.getNextExecutionTime());
    }

    if (request.getScheduleType() != null) {
      job.setScheduleType(job.getScheduleType());

      LocalDateTime nextExecution = calculateInitialExecutionTime(job);
      job.setNextExecutionTime(nextExecution);
    }

    if (request.getParameters() != null) {
      job.setParameters(request.getParameters());
    }

    if (request.getFixedDelayMs() != null) {
      job.setFixedDelay(request.getFixedDelayMs());
    }

    Job savedJob = jobRepository.save(job);
    return JobMapper.jobResponseDTO(savedJob);
  }

  private void validateScheduleRequirements(CreateJobRequestDTO request) {
    switch (request.getScheduleType()) {
      case CRON:
        if (request.getCronExpression() == null || request.getCronExpression().isBlank()) {
          throw new IllegalArgumentException("Cron expression is required for CRON schedule type");
        }
        validateCronExpression(request.getCronExpression());
        break;

      case FIXED_DELAY:
        if (request.getFixedDelayMs() == null || request.getFixedDelayMs() <= 0L) {
          throw new IllegalArgumentException("Fixed delay must be positive for FIXED_DELAY schedule type");
        }
        break;

      case ONE_TIME:
        break;
    }
  }

  private void validateCronExpression(String cronExpression) {
    try {
      org.springframework.scheduling.support.CronExpression.parse(cronExpression);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid cron expression: " + e.getMessage());
    }
  }

  private LocalDateTime calculateInitialExecutionTime(Job job) {
    switch (job.getScheduleType()) {
      case CRON:
        return calculateNextCronTime(job.getCronExpression());

      case FIXED_DELAY:
        return LocalDateTime.now();

      case ONE_TIME:
        return job.getNextExecutionTime() != null
            ? job.getNextExecutionTime()
            : LocalDateTime.now();

      default:
        throw new IllegalArgumentException("Unknown schedule type");
    }
  }

  private LocalDateTime calculateNextCronTime(String cronExpressionStr) {
    org.springframework.scheduling.support.CronExpression cronExpression = org.springframework.scheduling.support.CronExpression
        .parse(cronExpressionStr);
    return cronExpression.next(LocalDateTime.now());
  }
}
