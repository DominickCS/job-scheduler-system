package com.dominickcs.job_scheduler_system.dto;

import java.time.LocalDateTime;

import com.dominickcs.job_scheduler_system.model.JobStatus;
import com.dominickcs.job_scheduler_system.model.JobType;
import com.dominickcs.job_scheduler_system.model.ScheduleType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobResponseDTO {
  private Long id;
  private String name;
  private String description;
  private JobType jobType;
  private ScheduleType scheduleType;
  private String cronExpression;
  private Long fixedDelayMs;
  private JobStatus status;
  private Boolean enabled;
  private LocalDateTime nextExecutionTime;
  private LocalDateTime lastExecutionTime;
  private Long successCount;
  private Long failureCount;
  private String lastErrorMessage;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private Long totalExecutions; // successCount + failureCount
  private Double successRate; // successCount / totalExecutions
}
