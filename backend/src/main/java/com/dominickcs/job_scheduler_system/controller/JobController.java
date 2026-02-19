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

import com.dominickcs.job_scheduler_system.dto.CreateJobRequestDTO;
import com.dominickcs.job_scheduler_system.dto.JobExecutionResponseDTO;
import com.dominickcs.job_scheduler_system.dto.JobResponseDTO;
import com.dominickcs.job_scheduler_system.dto.UpdateJobRequestDTO;
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
  public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody CreateJobRequestDTO createJobRequestDTO) {
    JobResponseDTO response = jobService.createJob(createJobRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/jobs/update/{job_id}")
  public ResponseEntity<JobResponseDTO> updateJob(@PathVariable("job_id") Long id,
      @Valid @RequestBody UpdateJobRequestDTO updateJobRequestDTO) {
    JobResponseDTO response = jobService.updateJob(id, updateJobRequestDTO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

  @PostMapping("/jobs/delete/{job_id}")
  public String deleteJob(@PathVariable("job_id") Long id) {
    jobService.deleteJob(id);
    return ("Deleted job with id: " + id);
  }

  @PostMapping("/jobs/pause/{job_id}")
  public ResponseEntity<JobResponseDTO> pauseJob(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.pauseJob(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/jobs/resume/{job_id}")
  public ResponseEntity<JobResponseDTO> resumeJob(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.resumeJob(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/jobs/trigger/{job_id}")
  public ResponseEntity<JobResponseDTO> triggerJobNow(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.triggerJobNow(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/jobs/{job_id}")
  public ResponseEntity<JobResponseDTO> getJob(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.getJob(id);
    return ResponseEntity.status(HttpStatus.FOUND).body(response);
  }

  @GetMapping("/executions")
  public List<JobExecutionResponseDTO> getLatestExecutions() {
    return jobService.getAllExecutions();
  }

  @GetMapping("/jobs/{id}/executions")
  public List<JobExecutionResponseDTO> getJobExecutions(@PathVariable("job_id") Long id) {
    return jobService.getJobExecutions(id);
  }

  @GetMapping("/jobs")
  public List<Job> getAllJobs() {
    return jobService.getAllJobs();
  }
}
