package com.springboot.joblist.exception;

public class RequestIsNotValidException extends Exception{
    public RequestIsNotValidException(String message) {
        super(message);
    }
}