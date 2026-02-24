package com.dominickcs.job_scheduler_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequest;
import com.dominickcs.job_scheduler_system.dto.JobExecutionResponse;
import com.dominickcs.job_scheduler_system.dto.JobResponse;
import com.dominickcs.job_scheduler_system.dto.UpdateJobRequest;
import com.dominickcs.job_scheduler_system.model.Job;
import com.dominickcs.job_scheduler_system.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {
  @Autowired
  private JobService jobService;

  @PostMapping("/jobs/create")
  public ResponseEntity<JobResponse> createJob(@Valid @RequestBody CreateJobRequest createJobRequest) {
    JobResponse response = jobService.createJob(createJobRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/jobs/update/{job_id}")
  public ResponseEntity<JobResponse> updateJob(@PathVariable("job_id") Long id,
      @Valid @RequestBody UpdateJobRequest updateJobRequest) {
    JobResponse response = jobService.updateJob(id, updateJobRequest);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

  @PostMapping("/jobs/delete/{job_id}")
  public String deleteJob(@PathVariable("job_id") Long id) {
    jobService.deleteJob(id);
    return ("Deleted job with id: " + id);
  }

  @PostMapping("/jobs/pause/{job_id}")
  public ResponseEntity<JobResponse> pauseJob(@PathVariable("job_id") Long id) {
    JobResponse response = jobService.pauseJob(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/jobs/resume/{job_id}")
  public ResponseEntity<JobResponse> resumeJob(@PathVariable("job_id") Long id) {
    JobResponse response = jobService.resumeJob(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/jobs/trigger/{job_id}")
  public ResponseEntity<JobResponse> triggerJobNow(@PathVariable("job_id") Long id) {
    JobResponse response = jobService.triggerJobNow(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/jobs/{job_id}")
  public ResponseEntity<JobResponse> getJob(@PathVariable("job_id") Long id) {
    JobResponse response = jobService.getJob(id);
    return ResponseEntity.status(HttpStatus.FOUND).body(response);
  }

  @GetMapping("/executions")
  public List<JobExecutionResponse> getLatestExecutions() {
    return jobService.getAllExecutions();
  }

  @GetMapping("/jobs/{id}/executions")
  public List<JobExecutionResponse> getJobExecutions(@PathVariable("job_id") Long id) {
    return jobService.getJobExecutions(id);
  }

  @GetMapping("/jobs")
  public List<Job> getAllJobs() {
    return jobService.getAllJobs();
  }
}
