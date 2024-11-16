package com.deinterlab.userservice.model;

import com.deinterlab.userservice.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * Represents an authentication response with status, message, token, and user data.
 */
public class AuthResponse extends BaseRestResponse<UserDTO> {

    private final String token;

    /**
     * Full constructor to create an AuthResponse.
     *
     * @param status  HTTP status code.
     * @param message Response message.
     * @param token   JWT token.
     * @param data    User data.
     */
    @JsonCreator
    public AuthResponse(@JsonProperty("status") int status,
                        @JsonProperty("message") String message,
                        @JsonProperty("token") String token,
                        @JsonProperty("data") UserDTO data) {
        super(status, message, data);
        this.token = token;
    }

    /**
     * Constructor to create an AuthResponse with only a token.
     *
     * @param token JWT token.
     */
    public AuthResponse(String token) {
        this(HttpStatus.OK.value(), "User authenticated", token, null);
    }

    /**
     * Constructor to create an AuthResponse with a token and user data.
     *
     * @param token JWT token.
     * @param data  User data.
     */
    public AuthResponse(String token, UserDTO data) {
        this(HttpStatus.OK.value(), "User authenticated", token, data);
    }

    /**
     * Constructor to create an AuthResponse with status and message.
     *
     * @param status  HTTP status code.
     * @param message Response message.
     */
    public AuthResponse(int status, String message) {
        this(status, message, null, null);
    }

    /**
     * Constructor to create an AuthResponse with status, message, and token.
     *
     * @param status  HTTP status code.
     * @param message Response message.
     * @param token   JWT token.
     */
    public AuthResponse(int status, String message, String token) {
        this(status, message, token, null);
    }

    /**
     * Gets the JWT token.
     *
     * @return JWT token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns a string representation of the AuthResponse.
     *
     * @return String representation of AuthResponse.
     */
    @Override
    public String toString() {
        return new StringBuilder()
                .append("AuthResponse{")
                .append("token='").append(token).append('\'')
                .append(", data=").append(getData())
                .append('}')
                .toString();
    }
}
