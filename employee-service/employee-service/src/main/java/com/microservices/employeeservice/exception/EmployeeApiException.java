package com.microservices.employeeservice.exception;

import org.springframework.http.HttpStatus;

public class EmployeeApiException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public EmployeeApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
