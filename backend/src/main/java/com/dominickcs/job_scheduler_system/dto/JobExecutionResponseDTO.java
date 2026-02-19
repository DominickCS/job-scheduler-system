package com.dominickcs.job_scheduler_system.dto;

import java.time.LocalDateTime;
import com.dominickcs.job_scheduler_system.model.JobExecutionStatus;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class JobExecutionResponseDTO {

  private Long id;
  private Long jobId;
  private String jobName;
  private JobExecutionStatus status;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Long durationMs;
  private String errorMessage;
  private String message;
}
