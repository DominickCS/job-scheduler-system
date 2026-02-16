package com.dominickcs.job_scheduler_system.jobparameters;

import lombok.Data;

@Data
public class HealthCheckParameters {
  private String url;
  private int expectedStatus = 200;
  private int timeoutSeconds = 5;
}
