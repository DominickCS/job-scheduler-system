package com.dominickcs.job_scheduler_system.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
  @Column(nullable = true)
  private Long fixedDelay;
  @Nullable
  private String cronExpression;
  private long successCounter;
  private long failureCounter;
  @Column(columnDefinition = "TEXT")
  private String lastErrorMessage;
  @Column(columnDefinition = "TEXT")
  private String jobParameters;
  private Boolean isEnabled;
  @Enumerated(EnumType.STRING)
  private JobStatus jobStatus;
  @Nullable
  private LocalDateTime lastExecution;
  private LocalDateTime nextExecution;
  @JsonIgnore
  @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JobExecution> jobExecutions;

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
