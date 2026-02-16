package com.dominickcs.job_scheduler_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.dominickcs.job_scheduler_system.config.DotEnvConfig;
import com.dominickcs.job_scheduler_system.executor.EmailNotificationExecutor;
import com.dominickcs.job_scheduler_system.executor.JobExecutorResult;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobType;

@SpringBootTest
@ContextConfiguration(classes = JobSchedulerSystemApplication.class, initializers = DotEnvConfig.class)
class MailExecutorJobTest {

  @Autowired
  EmailNotificationExecutor executor;

  @Test
  public void sendMail() {
    JobExecutorResult result;
    Job mailTestJob = new Job();
    mailTestJob.setJobType(JobType.EMAIL_NOTIFICATION);
    mailTestJob.setParameters(
        "{\"from\":\"dominick.smith938@gmail.com\",\"to\":\"dominick.smith938@gmail.com\",\"subject\":\"Test Email\",\"body\":\"This is a test!\"}");

    result = executor.execute(mailTestJob);

    System.out.println("Success: " + result.isJobSuccessful());
    System.out.println("Message: " + result.getMessage());
    System.out.println("Execution time: " + result.getExecutionTimeMs() + "ms");
  }

}
