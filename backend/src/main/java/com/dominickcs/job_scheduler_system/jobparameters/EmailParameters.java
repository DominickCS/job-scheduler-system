package com.dominickcs.job_scheduler_system.jobparameters;

import lombok.Data;

@Data
public class EmailParameters {
  private String from;
  private String to;
  private String subject;
  private String body;
}
