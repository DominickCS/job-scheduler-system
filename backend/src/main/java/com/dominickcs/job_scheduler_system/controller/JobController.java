package com.dominickcs.job_scheduler_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequestDTO;
import com.dominickcs.job_scheduler_system.dto.JobResponseDTO;
import com.dominickcs.job_scheduler_system.dto.UpdateJobRequestDTO;
import com.dominickcs.job_scheduler_system.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/job")
public class JobController {
  @Autowired
  private JobService jobService;

  @PostMapping("/create")
  public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody CreateJobRequestDTO createJobRequestDTO) {
    JobResponseDTO response = jobService.createJob(createJobRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/update/{job_id}")
  public ResponseEntity<JobResponseDTO> updateJob(@PathVariable("job_id") Long id,
      @Valid @RequestBody UpdateJobRequestDTO updateJobRequestDTO) {
    JobResponseDTO response = jobService.updateJob(id, updateJobRequestDTO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

  @PostMapping("/delete/{job_id}")
  public String deleteJob(@PathVariable("job_id") Long id) {
    jobService.deleteJob(id);
    return ("Deleted job with id: " + id);
  }

  @PostMapping("/pause/{job_id}")
  public ResponseEntity<JobResponseDTO> pauseJob(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.pauseJob(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/resume/{job_id}")
  public ResponseEntity<JobResponseDTO> resumeJob(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.resumeJob(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/trigger/{job_id}")
  public ResponseEntity<JobResponseDTO> triggerJobNow(@PathVariable("job_id") Long id) {
    JobResponseDTO response = jobService.triggerJobNow(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

}
