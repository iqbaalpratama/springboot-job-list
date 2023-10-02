package com.springboot.joblist.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.joblist.exception.UserNotFoundException;
import com.springboot.joblist.exception.JobNotFoundException;
import com.springboot.joblist.exception.RequestIsNotValidException;
import com.springboot.joblist.exception.JobFailedToRetrieveException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {

    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> constraintViolationException(ConstraintViolationException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Status", "Constraint violation error");
        response.put("Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotFoundException(UserNotFoundException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Status", "User not found Error");
        response.put("Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<Map<String, String>> jobNotFoundException(JobNotFoundException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Status", "Job not found error");
        response.put("Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(JobFailedToRetrieveException.class)
    public ResponseEntity<Map<String, String>> jobFailedToRetrieveException(JobFailedToRetrieveException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Status", "Failed to retrieve job error");
        response.put("Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> UsernameNotFoundException(UsernameNotFoundException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Status", "Username not found error");
        response.put("Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(RequestIsNotValidException.class)
    public ResponseEntity<Map<String, String>> requestIsNotValidException(RequestIsNotValidException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Status", "Request is not valid error");
        response.put("Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}