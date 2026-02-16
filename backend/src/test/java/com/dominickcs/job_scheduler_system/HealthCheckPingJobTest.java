package com.dominickcs.job_scheduler_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.dominickcs.job_scheduler_system.config.DotEnvConfig;
import com.dominickcs.job_scheduler_system.executor.HealthCheckPingExecutor;
import com.dominickcs.job_scheduler_system.executor.JobExecutorResult;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobType;

@SpringBootTest
@ContextConfiguration(classes = JobSchedulerSystemApplication.class, initializers = DotEnvConfig.class)
class HealthCheckPingJobTest {

  @Autowired
  HealthCheckPingExecutor healthCheckPingExecutor;

  @Test
  public void healthyCheck() {
    Job healthyJob = new Job();
    JobExecutorResult result;

    healthyJob.setJobType(JobType.HEALTH_CHECK_PING);
    healthyJob.setJobName("HEALTHY JOB TEST");
    healthyJob.setParameters(
        "{\"url\":\"https://www.google.com\"," +
            "\"expectedStatus\":200," +
            "\"timeoutSeconds\":5}");

    System.out.println("Executing Healthy Job:");

    if (healthCheckPingExecutor.supports(healthyJob.getJobType())) {
      result = healthCheckPingExecutor.execute(healthyJob);
      System.out.println("Job Success: " + result.isJobSuccessful());
      System.out.println("Job Message: " + result.getMessage());
      System.out.println("Job Execution Time (ms) " + result.getExecutionTimeMs());
    } else {
      System.out.println("Job type unsupported!");
    }
  }

  @Test
  public void unhealthy() {
    Job unhealthyJob = new Job();
    JobExecutorResult result;

    unhealthyJob.setJobType(JobType.HEALTH_CHECK_PING);
    unhealthyJob.setJobName("UNHEALTHY JOB TEST");
    unhealthyJob.setParameters(
        "{\"url\":\"https://tools-httpstatus.pickup-services.com/404\"," +
            "\"expectedStatus\":200," +
            "\"timeoutSeconds\":5}");

    System.out.println("Executing Unhealthy Job:");

    if (healthCheckPingExecutor.supports(unhealthyJob.getJobType())) {
      result = healthCheckPingExecutor.execute(unhealthyJob);
      System.out.println("Job Success: " + result.isJobSuccessful());
      System.out.println("Job Message: " + result.getMessage());
      System.out.println("Job Execution Time (ms) " + result.getExecutionTimeMs());
    } else {
      System.out.println("Job type unsupported!");
    }

  }
}
