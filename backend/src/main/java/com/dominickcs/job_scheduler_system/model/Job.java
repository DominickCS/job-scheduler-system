package com.dominickcs.job_scheduler_system.model;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
  @Enumerated(EnumType.STRING)
  private JobType jobType;
  @Enumerated(EnumType.STRING)
  private ScheduleType scheduleType;
  @Nullable
  private long fixedDelay;
  @Nullable
  private String cronExpression;
  private long successCounter;
  private long failureCounter;
  private String lastErrorMessage;
  private String parameters;
  private boolean enabled;
  @Enumerated(EnumType.STRING)
  private JobStatus jobStatus;
  @Nullable
  private LocalDateTime lastExecutionTime;
  private LocalDateTime nextExecutionTime;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    modifiedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    modifiedAt = LocalDateTime.now();
  }

}
