package com.deinterlab.userservice.model;

import org.springframework.http.HttpStatus;

public class AuthResponse extends BaseRestResponse<String> {
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        super(HttpStatus.OK.value(), "User authenticated", null);
        this.token = token;
    }

    public AuthResponse(int status, String message) {
        super(status, message, null);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
