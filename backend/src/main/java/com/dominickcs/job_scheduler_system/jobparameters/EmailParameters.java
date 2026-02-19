package com.dominickcs.job_scheduler_system.jobparameters;

import lombok.Data;

@Data
public class EmailParameters {
  private String emailFrom;
  private String emailTo;
  private String emailSubject;
  private String emailBody;
}
