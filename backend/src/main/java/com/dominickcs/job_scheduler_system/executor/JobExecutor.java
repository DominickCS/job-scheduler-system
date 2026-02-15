package com.dominickcs.job_scheduler_system.executor;

import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobType;

public interface JobExecutor {
  JobExecutorResult execute(Job job);

  boolean supports(JobType jobType);
}
