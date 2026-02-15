package com.dominickcs.job_scheduler_system.executor;

import lombok.Data;

@Data
public class JobExecutorResult {
  boolean jobSuccessful;
  String message;
  long executionTimeMs;

  public JobExecutorResult(boolean jobSuccessful, String message, long executionTimeMs) {
    this.jobSuccessful = jobSuccessful;
    this.message = message;
    this.executionTimeMs = executionTimeMs;
  }

}
