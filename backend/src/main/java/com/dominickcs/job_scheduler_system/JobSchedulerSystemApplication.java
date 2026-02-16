package com.dominickcs.job_scheduler_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.dominickcs.job_scheduler_system.config.DotEnvConfig;

@SpringBootApplication
@EnableScheduling
public class JobSchedulerSystemApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(JobSchedulerSystemApplication.class);
    app.addInitializers(new DotEnvConfig());
    app.run(args);
  }
}
