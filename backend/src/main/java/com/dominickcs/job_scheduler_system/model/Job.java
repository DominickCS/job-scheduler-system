package com.dominickcs.job_scheduler_system.model;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "jobs")
@Data
public class Job {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String jobName;
  private String jobDescription;
  private JobType jobType;
  @Nullable
  private long fixedDelay;
  @Nullable
  private String cronExpression;
  private long successCounter;
  private long failureCounter;
  private String lastErrorMessage;
}
