package com.dominickcs.job_scheduler_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dominickcs.job_scheduler_system.dto.CreateJobRequestDTO;
import com.dominickcs.job_scheduler_system.dto.JobResponseDTO;
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

}
