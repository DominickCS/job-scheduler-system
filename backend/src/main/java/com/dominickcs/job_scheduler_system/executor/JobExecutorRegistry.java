package com.dominickcs.job_scheduler_system.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dominickcs.job_scheduler_system.model.JobType;

@Component
public class JobExecutorRegistry {
  private final Map<JobType, JobExecutor> executorMap = new HashMap<>();

  public JobExecutorRegistry(List<JobExecutor> jobExecutors) {
    for (JobExecutor executor : jobExecutors) {
      for (JobType jobType : JobType.values()) {
        if (executor.supports(jobType)) {
          executorMap.put(jobType, executor);
        }
      }
    }
  }

  public JobExecutor getExecutor(JobType jobType) {
    JobExecutor executor = executorMap.get(jobType);
    if (executor == null) {
      throw new IllegalArgumentException("No executor found for job type: " + jobType);
    }
    return executor;
  }

}
