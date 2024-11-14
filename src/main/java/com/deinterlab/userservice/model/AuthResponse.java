package com.deinterlab.userservice.model;

import com.deinterlab.userservice.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class AuthResponse extends BaseRestResponse<UserDTO> {

    private String token;
    private UserDTO data;

    // Constructor with arguments
    @JsonCreator
    public AuthResponse(@JsonProperty("status") int status,
                        @JsonProperty("message") String message,
                        @JsonProperty("token") String token,
                        @JsonProperty("data") UserDTO data) {
        super(status, message, data);
        this.token = token;
        this.data = data;
    }


    /**
     * Create a new AuthResponse object
     *
     * @param token JWT token
     */
    public AuthResponse(String token) {
        super(HttpStatus.OK.value(), "User authenticated", null);
        this.token = token;
    }

    /**
     * Create a new AuthResponse object
     *
     * @param token JWT token
     * @param data  response data
     */
    public AuthResponse(String token, UserDTO data) {
        super(HttpStatus.OK.value(), "User authenticated", data);
        this.token = token;
        this.data = data;
    }

    /**
     * Create a new AuthResponse object
     *
     * @param status  response status
     * @param message response message
     */
    public AuthResponse(int status, String message) {
        super(status, message, null);
    }

    /**
     * Create a new AuthResponse object
     *
     * @param status  response status
     * @param message response message
     * @param token   JWT token
     */
    public AuthResponse(int status, String message, String token) {
        super(status, message, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public UserDTO getData() {
        return data;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
