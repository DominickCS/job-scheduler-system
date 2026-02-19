package com.dominickcs.job_scheduler_system;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.dominickcs.job_scheduler_system.config.DotEnvConfig;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.model.JobStatus;
import com.dominickcs.job_scheduler_system.model.JobType;
import com.dominickcs.job_scheduler_system.model.ScheduleType;
import com.dominickcs.job_scheduler_system.repository.JobRepository;

@SpringBootTest
@ContextConfiguration(classes = JobSchedulerSystemApplication.class, initializers = DotEnvConfig.class)
class CreateTestJobs {

  @Autowired
  private JobRepository jobRepository;

  @Test
  void createOneTime() {
    Job job = new Job();

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setJobName("(TEST) ONE_TIME JOB");
    job.setJobType(JobType.EMAIL_NOTIFICATION);
    job.setJobParameters(
        "{\"from\":\"no-reply@dominickcs.com\",\"to\":\"dominick.smith938@gmail.com\",\"subject\":\"MailExecutorJobTest - Unit Test - ONE_TIME\",\"body\":\"This is a test!\"}");
    job.setIsEnabled(true);
    job.setJobDescription("Execution of the CreateTestJob Unit Test - EMAIL_NOTIFICATION");
    job.setNextExecution(LocalDateTime.now().minusMinutes(1));
    job.setScheduleType(ScheduleType.ONE_TIME);
    jobRepository.save(job);
  }

  @Test
  void createFixedJob() {
    Job job = new Job();

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setJobName("(TEST) FIXED_DELAY JOB");
    job.setJobType(JobType.EMAIL_NOTIFICATION);
    job.setJobParameters(
        "{\"from\":\"no-reply@dominickcs.com\",\"to\":\"dominick.smith938@gmail.com\",\"subject\":\"MailExecutorJobTest - Unit Test - FIXED_DELAY\",\"body\":\"This is a test!\"}");
    job.setIsEnabled(true);
    job.setJobDescription("Execution of the CreateTestJob Unit Test - EMAIL_NOTIFICATION");
    job.setNextExecution(LocalDateTime.now().minusMinutes(1));
    job.setScheduleType(ScheduleType.FIXED_DELAY);
    job.setFixedDelay(5000L);
    jobRepository.save(job);
  }

  @Test
  void createCronJob() {
    Job job = new Job();

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setJobName("(TEST) CRON JOB");
    job.setJobType(JobType.EMAIL_NOTIFICATION);
    job.setJobParameters(
        "{\"from\":\"no-reply@dominickcs.com\",\"to\":\"dominick.smith938@gmail.com\",\"subject\":\"MailExecutorJobTest - Unit Test - CRON\",\"body\":\"This is a test!\"}");
    job.setIsEnabled(true);
    job.setJobDescription("Execution of the CreateTestJob Unit Test - EMAIL_NOTIFICATION");
    job.setNextExecution(LocalDateTime.now().minusMinutes(1));
    job.setScheduleType(ScheduleType.CRON);
    job.setCronExpression("0 * * * * *");
    jobRepository.save(job);
  }
}
