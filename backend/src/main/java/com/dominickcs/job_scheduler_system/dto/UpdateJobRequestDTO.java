package com.dominickcs.job_scheduler_system.dto;

import java.time.LocalDateTime;

import com.dominickcs.job_scheduler_system.model.ScheduleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateJobRequestDTO {
  @NotBlank(message = "A job name is required.")
  @Size(min = 4, message = "The job name must be at least four (4) characters long.")
  private String jobName;
  @Size(max = 500, message = "The job description cannot exceed 500 characters.")
  private String jobDescription;
  @NotNull(message = "A schedule type must be specified.")
  private ScheduleType scheduleType;
  private long fixedDelayMs;
  private String cronExpression;
  private LocalDateTime nextExecutionTime;
  @NotBlank
  private String parameters;
  private boolean enabled = true;
}
