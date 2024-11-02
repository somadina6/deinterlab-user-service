package com.deinterlab.userservice.exception;

import com.deinterlab.userservice.model.BaseRestResponse;

public class ErrorResponse extends BaseRestResponse<String> {

    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        super(status, message, null);
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message) {
        super(500, message, null);
        this.message = message;
        this.status = 500;
    }

    // Getters and setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
