package com.dominickcs.job_scheduler_system.mapper;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequestDTO;
import com.dominickcs.job_scheduler_system.dto.JobListResponseDTO;
import com.dominickcs.job_scheduler_system.dto.JobResponseDTO;
import com.dominickcs.job_scheduler_system.model.Job;

public class JobMapper {
  public static JobResponseDTO jobResponseDTO(Job job) {
    return jobResponseDTO(job).builder()
        .createdAt(job.getCreatedAt())
        .cronExpression(job.getCronExpression())
        .id(job.getId())
        .jobType(job.getJobType())
        .description(job.getJobDescription())
        .enabled(job.isEnabled())
        .failureCount(job.getFailureCounter())
        .fixedDelayMs(job.getFixedDelay())
        .lastErrorMessage(job.getLastErrorMessage())
        .modifiedAt(job.getModifiedAt())
        .name(job.getJobName())
        .lastExecutionTime(job.getLastExecutionTime())
        .nextExecutionTime(job.getNextExecutionTime())
        .status(job.getJobStatus())
        .successCount(job.getSuccessCounter())
        .totalExecutions(job.getSuccessCounter() + job.getFailureCounter())
        .scheduleType(job.getScheduleType())
        .successRate(calculateSuccessRate(job))
        .build();
  }

  public static JobListResponseDTO jobListResponseDTO(Job job) {
    return JobListResponseDTO.builder()
        .id(job.getId())
        .name(job.getJobName())
        .enabled(job.isEnabled())
        .jobType(job.getJobType())
        .failureCount(job.getFailureCounter())
        .nextExecutionTime(job.getNextExecutionTime())
        .status(job.getJobStatus())
        .successCount(job.getSuccessCounter())
        .build();
  }

  public static Job toEntity(CreateJobRequestDTO request) {
    Job job = new Job();
    job.setJobName(request.getJobName());
    job.setJobDescription(request.getJobDescription());
    job.setJobType(request.getJobType());
    job.setScheduleType(request.getScheduleType());
    job.setFixedDelay(request.getFixedDelayMs());
    job.setCronExpression(request.getCronExpression());
    job.setParameters(request.getParameters());
    job.setEnabled(request.isEnabled());
    job.setNextExecutionTime(request.getNextExecutionTime());
    return job;
  }

  private static Double calculateSuccessRate(Job job) {
    long total = job.getSuccessCounter() + job.getFailureCounter();
    if (total == 0)
      return null;
    return (double) job.getSuccessCounter() / total * 100;
  }
}
