package com.dominickcs.job_scheduler_system.executor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.stereotype.Component;

import com.dominickcs.job_scheduler_system.jobparameters.HealthCheckParameters;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HealthCheckPingExecutor implements JobExecutor {

  private ObjectMapper objectMapper;

  private final HttpClient httpClient = HttpClient.newBuilder()
      .version(Version.HTTP_1_1)
      .followRedirects(Redirect.NORMAL)
      .build();

  public HealthCheckPingExecutor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public JobExecutorResult execute(Job job) {
    long startTime = System.currentTimeMillis();
    try {
      HealthCheckParameters healthCheckParameters = objectMapper.readValue(job.getParameters(),
          HealthCheckParameters.class);
      HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(healthCheckParameters.getUrl()))
          .timeout(java.time.Duration.ofSeconds(healthCheckParameters.getTimeoutSeconds())).build();
      HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());
      long executionTimeMs = System.currentTimeMillis() - startTime;

      int statusCode = httpResponse.statusCode();
      if (statusCode == healthCheckParameters.getExpectedStatus()) {
        return new JobExecutorResult(true, "Health Check Passed! Status Returned: " + httpResponse.statusCode(),
            executionTimeMs);
      } else {
        return new JobExecutorResult(false, "Health Check Failed! Status Returned " + httpResponse.statusCode(),
            executionTimeMs);
      }
    } catch (IOException ioException) {
      return new JobExecutorResult(false, "JOB FAILURE (" + job.getJobName() + ") | IOException " + ioException, 0L);
    } catch (InterruptedException interruptedException) {
      return new JobExecutorResult(false,
          "JOB FAILURE (" + job.getJobName() + ") | InterruptedException " + interruptedException, 0L);
    }
  }

  @Override
  public boolean supports(JobType jobType) {
    if (jobType == JobType.HEALTH_CHECK_PING) {
      return true;
    } else {
      return false;
    }
  }
}
