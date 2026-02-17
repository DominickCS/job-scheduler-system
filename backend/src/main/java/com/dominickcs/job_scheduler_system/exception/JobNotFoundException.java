package com.dominickcs.job_scheduler_system.exception;

public class JobNotFoundException extends RuntimeException {
  public JobNotFoundException(String message) {
    super(message);
  }
}
