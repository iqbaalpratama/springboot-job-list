package com.springboot.joblist.exception;

public class JobFailedToRetrieveException extends Exception{
    public JobFailedToRetrieveException(String message) {
        super(message);
    }
}