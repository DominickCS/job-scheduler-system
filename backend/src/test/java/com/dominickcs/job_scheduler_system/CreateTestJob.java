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
import com.dominickcs.job_scheduler_system.repository.JobRepository;
import com.dominickcs.job_scheduler_system.service.ScheduleService;

@SpringBootTest
@ContextConfiguration(classes = JobSchedulerSystemApplication.class, initializers = DotEnvConfig.class)
class CreateTestJob {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private ScheduleService scheduleService;

  @Test
  void create() {
    Job job = new Job();

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setJobName("TEST JOB");
    job.setJobType(JobType.EMAIL_NOTIFICATION);
    job.setParameters(
        "{\"from\":\"no-reply@dominickcs.com\",\"to\":\"dominick.smith938@gmail.com\",\"subject\":\"MailExecutorJobTest - Unit Test\",\"body\":\"This is a test!\"}");
    job.setEnabled(true);
    job.setJobDescription("Execution of the CreateTestJob Unit Test - EMAIL_NOTIFICATION");
    job.setNextExecutionTime(LocalDateTime.now().minusMinutes(1));
    jobRepository.save(job);

    scheduleService.processJobs();
  }

  @Test
  void createTwo() {
    Job job = new Job();

    job.setJobStatus(JobStatus.SCHEDULED);
    job.setJobName("TEST JOB TWO");
    job.setJobType(JobType.EMAIL_NOTIFICATION);
    job.setParameters(
        "{\"from\":\"no-reply@dominickcs.com\",\"to\":\"dominick.smith938@gmail.com\",\"subject\":\"MailExecutorJobTest - Unit Test - Two\",\"body\":\"This is a test!\"}");
    job.setEnabled(true);
    job.setJobDescription("Execution of the CreateTestJob Unit Test - EMAIL_NOTIFICATION");
    job.setNextExecutionTime(LocalDateTime.now().minusMinutes(1));
    jobRepository.save(job);

    scheduleService.processJobs();
  }
}
