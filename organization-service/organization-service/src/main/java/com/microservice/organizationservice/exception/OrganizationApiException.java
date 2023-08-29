package com.microservice.organizationservice.exception;

import org.springframework.http.HttpStatus;

public class OrganizationApiException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public OrganizationApiException(HttpStatus status, String message) {
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
