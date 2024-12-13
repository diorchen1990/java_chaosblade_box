package com.chaos.platform.exception;

public class ExperimentExecutionException extends RuntimeException {
    public ExperimentExecutionException(String message) {
        super(message);
    }
    
    public ExperimentExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
} 