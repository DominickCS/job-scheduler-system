package com.dominickcs.job_scheduler_system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequestDTO;
import com.dominickcs.job_scheduler_system.dto.JobExecutionResponseDTO;
import com.dominickcs.job_scheduler_system.dto.JobResponseDTO;
import com.dominickcs.job_scheduler_system.dto.UpdateJobRequestDTO;
import com.dominickcs.job_scheduler_system.exception.JobNotFoundException;
import com.dominickcs.job_scheduler_system.mapper.JobMapper;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobExecution;
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
    job.setNextExecution(nextExecution);

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

    if (request.getNextExecution() != null) {
      job.setNextExecution(request.getNextExecution());
    }

    if (request.getScheduleType() != null) {
      job.setScheduleType(job.getScheduleType());

      LocalDateTime nextExecution = calculateInitialExecutionTime(job);
      job.setNextExecution(nextExecution);
    }

    if (request.getJobParameters() != null) {
      job.setJobParameters(request.getJobParameters());
    }

    if (request.getFixedDelay() != null) {
      job.setFixedDelay(request.getFixedDelay());
    }

    Job savedJob = jobRepository.save(job);
    return JobMapper.jobResponseDTO(savedJob);
  }

  public JobResponseDTO pauseJob(Long id) {
    Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

    job.setJobStatus(JobStatus.PAUSED);
    job.setIsEnabled(false);

    Job savedJob = jobRepository.save(job);
    return JobMapper.jobResponseDTO(savedJob);
  }

  public JobResponseDTO resumeJob(Long id) {
    Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setIsEnabled(true);

    Job savedJob = jobRepository.save(job);
    return JobMapper.jobResponseDTO(savedJob);
  }

  public JobResponseDTO triggerJobNow(Long id) {
    Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

    job.setIsEnabled(true);
    job.setNextExecution(LocalDateTime.now());
    job.setJobStatus(JobStatus.SCHEDULED);

    Job savedJob = jobRepository.save(job);
    return JobMapper.jobResponseDTO(savedJob);
  }

  public void deleteJob(Long id) {
    if (!jobRepository.existsById(id)) {
      throw new JobNotFoundException("Job not found with id: " + id);
    }
    jobRepository.deleteById(id);
  }

  public List<JobExecutionResponseDTO> getJobExecutions(Long jobId) {
    if (!jobRepository.existsById(jobId)) {
      throw new JobNotFoundException("Job not found with id: " + jobId);
    }

    List<JobExecution> executions = jExecutionRepository
        .findByJobIdOrderByStartTimeDesc(jobId);

    return executions.stream()
        .map(JobMapper::toExecutionResponse)
        .toList();
  }

  public List<JobExecutionResponseDTO> getAllExecutions() {
    List<JobExecution> executions = jExecutionRepository
        .findTop10ByOrderByStartTimeDesc();

    return executions.stream()
        .map(JobMapper::toExecutionResponse)
        .toList();
  }

  public JobResponseDTO getJob(Long id) {
    Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

    return JobMapper.jobResponseDTO(job);
  }

  public List<Job> getAllJobs() {
    return jobRepository.findAll();
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
        if (request.getFixedDelay() == null || request.getFixedDelay() <= 0L) {
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
        return job.getNextExecution() != null
            ? job.getNextExecution()
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
