package com.springboot.joblist.exception;

public class JobNotFoundException extends Exception{
    public JobNotFoundException(String message) {
        super(message);
    }
}
