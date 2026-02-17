package com.dominickcs.job_scheduler_system.dto;

import java.time.LocalDateTime;
import com.dominickcs.job_scheduler_system.model.JobType;
import com.dominickcs.job_scheduler_system.model.JobStatus;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class JobListResponseDTO {

  private Long id;
  private String name;
  private JobType jobType;
  private JobStatus status;
  private Boolean enabled;
  private LocalDateTime nextExecutionTime;
  private Long successCount;
  private Long failureCount;

}
