package com.deinterlab.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {

    private HttpStatus status;

    public UserException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public UserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
