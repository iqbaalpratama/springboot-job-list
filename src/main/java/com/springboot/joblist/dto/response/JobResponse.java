package com.springboot.joblist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

import com.springboot.joblist.entity.Job;

import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobResponse {
    private String location;
    private List<Job> data;
}
