package com.springboot.joblist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.joblist.entity.Job;
import com.springboot.joblist.exception.JobFailedToRetrieveException;
import com.springboot.joblist.exception.JobNotFoundException;
import com.springboot.joblist.service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/jobs")
public class JobController {

    private final JobService jobService;
        
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Job[]>  getAllJob() throws JobFailedToRetrieveException{
        Job[] jobs = jobService.getAllJob();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Job> register(@PathVariable String id) throws JobNotFoundException, JobFailedToRetrieveException{
        Job job = jobService.getJob(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }
}
