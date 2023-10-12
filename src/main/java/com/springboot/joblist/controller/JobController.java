package com.springboot.joblist.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.joblist.dto.response.APIResponse;
import com.springboot.joblist.dto.response.JobResponse;
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
    public ResponseEntity<APIResponse>  getAllJob() throws JobFailedToRetrieveException{
        Map<String, List<Job>> jobs = jobService.getAllJob();
        List<JobResponse> jobResponse = new ArrayList<>();
        jobs.forEach((k,v) -> {
            JobResponse job = new JobResponse();
            job.setData(v);
            job.setLocation(k);
            jobResponse.add(job);
        });
        APIResponse apiResponse = APIResponse.builder().message("Success").result(jobResponse).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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
