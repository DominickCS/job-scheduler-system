package com.dominickcs.job_scheduler_system.mapper;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequest;
import com.dominickcs.job_scheduler_system.dto.JobExecutionResponse;
import com.dominickcs.job_scheduler_system.dto.JobListResponse;
import com.dominickcs.job_scheduler_system.dto.JobResponse;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobExecution;

public class JobMapper {
  public static JobResponse jobResponseDTO(Job job) {
    return JobResponse.builder()
        .createdAt(job.getCreatedAt())
        .cronExpression(job.getCronExpression())
        .id(job.getId())
        .jobType(job.getJobType())
        .jobDescription(job.getJobDescription())
        .isEnabled(job.getIsEnabled())
        .failureCount(job.getFailureCounter())
        .fixedDelay(job.getFixedDelay())
        .lastErrorMessage(job.getLastErrorMessage())
        .modifiedAt(job.getModifiedAt())
        .jobName(job.getJobName())
        .lastExecution(job.getLastExecution())
        .nextExecution(job.getNextExecution())
        .status(job.getJobStatus())
        .successCount(job.getSuccessCounter())
        .totalExecutions(job.getSuccessCounter() + job.getFailureCounter())
        .scheduleType(job.getScheduleType())
        .successRate(calculateSuccessRate(job))
        .build();
  }

  public static JobListResponse jobListResponseDTO(Job job) {
    return JobListResponse.builder()
        .id(job.getId())
        .name(job.getJobName())
        .enabled(job.getIsEnabled())
        .jobType(job.getJobType())
        .failureCount(job.getFailureCounter())
        .nextExecutionTime(job.getNextExecution())
        .status(job.getJobStatus())
        .successCount(job.getSuccessCounter())
        .build();
  }

  public static Job toEntity(CreateJobRequest request) {
    Job job = new Job();
    job.setJobName(request.getJobName());
    job.setJobDescription(request.getJobDescription());
    job.setJobType(request.getJobType());
    job.setScheduleType(request.getScheduleType());
    job.setFixedDelay(request.getFixedDelay());
    job.setCronExpression(request.getCronExpression());
    job.setJobParameters(request.getJobParameters());
    job.setIsEnabled(request.getIsEnabled());
    job.setNextExecution(request.getNextExecution());
    return job;
  }

  public static JobExecutionResponse toExecutionResponse(JobExecution execution) {
    return JobExecutionResponse.builder()
        .id(execution.getId())
        .jobId(execution.getJob().getId())
        .jobName(execution.getJob().getJobName())
        .status(execution.getStatus())
        .startTime(execution.getStartTime())
        .endTime(execution.getEndTime())
        .durationMs(execution.getDurationMs())
        .errorMessage(execution.getErrorMessage())
        .message(execution.getMessage())
        .build();
  }

  private static Double calculateSuccessRate(Job job) {
    long total = job.getSuccessCounter() + job.getFailureCounter();
    if (total == 0)
      return null;
    return (double) job.getSuccessCounter() / total * 100;
  }
}
