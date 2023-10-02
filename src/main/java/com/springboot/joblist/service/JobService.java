package com.springboot.joblist.service;


import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.joblist.entity.Job;
import com.springboot.joblist.exception.JobFailedToRetrieveException;
import com.springboot.joblist.exception.JobNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JobService{

    @Transactional
	public Job[] getAllJob() throws JobFailedToRetrieveException {
        String jobsUrl = "https://dev6.dansmultipro.com/api/recruitment/positions.json";
		Duration timeoutDuration = Duration.ofMillis(5000);
		
		String messageBody = getRestAPIResources(jobsUrl, timeoutDuration);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Job[] result= null;
		try {
			JsonNode jobsJson = objectMapper.readTree(messageBody);
			result = objectMapper.convertValue(jobsJson, new TypeReference<Job[]>(){});
            return result;
		} catch (JsonProcessingException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new JobFailedToRetrieveException(e.getMessage());
		}
    }
    
	public Job getJob(String id) throws JobNotFoundException, JobFailedToRetrieveException {
        String jobsUrl = "https://dev6.dansmultipro.com/api/recruitment/positions/"+id;
		Duration timeoutDuration = Duration.ofMillis(5000);
		
		String messageBody = getRestAPIResources(jobsUrl, timeoutDuration);
		if(messageBody.equals("{}")){
			throw new JobNotFoundException("Job with id: "+ id + " is not found");
		}

		ObjectMapper objectMapper = new ObjectMapper();
		Job result= null;
		try {
			JsonNode jobsJson = objectMapper.readTree(messageBody);
			result = objectMapper.convertValue(jobsJson, new TypeReference<Job>(){});
            return result;
		}catch (JsonProcessingException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new JobFailedToRetrieveException(e.getMessage());
		}
    }

    private String getRestAPIResources(String url, Duration timeoutDuration) {
		WebClient client = WebClient.create();

		ResponseSpec responseSpec = client.get()
		    .uri(url)
		    .retrieve();

		String responseBody = responseSpec.bodyToMono(String.class)
							.timeout(timeoutDuration)
							.onErrorResume(throwable -> timeoutFallBackMethod(throwable))
							.block();
		

		if (responseBody.isEmpty()) {
			return null;
		}else {
			return responseBody;
		}
	}

	private Mono<? extends String> timeoutFallBackMethod(Throwable throwable) {
		return Mono.just("");
	}
}

