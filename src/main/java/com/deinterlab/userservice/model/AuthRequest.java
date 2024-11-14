package com.deinterlab.userservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthRequest {

    @Email(message = "Email should be valid")
    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotNull(message = "Password is mandatory")
    @NotBlank (message = "Password is mandatory")
    private String password;

    @NotNull(message = "First name is mandatory", groups = RegisterValidationGroup.class)
    private String firstName;

    @NotNull(message = "Last name is mandatory", groups = RegisterValidationGroup.class)
    private String lastName;

    public AuthRequest() {
    }

    public AuthRequest(String email, String password) {
        this(email, password, null, null);
    }

    public AuthRequest(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
