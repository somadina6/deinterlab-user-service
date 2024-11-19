package com.deinterlab.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, name = "email")
    @Email
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(name = "first_name")
    @NotNull(message = "First name is mandatory", groups = RegisterValidationGroup.class)
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "Last name is mandatory", groups = RegisterValidationGroup.class)
    private String lastName;

    @Column(name = "phone_number")
    @NotNull(message = "Phone number is mandatory", groups = RegisterValidationGroup.class)
    private String phoneNumber;

    @Column(nullable = false, name = "role")
    private String role = "ROLE_USER";  // Default role assigned directly

    public User() {
        // Default constructor
    }

    public User(String email, String password) {
        this(email, password, null, null, null,"ROLE_USER");
    }

    public User(String email, String password, String role) {
        this(email, password, null, null, null, role);
    }

    public User(String email, String password, String firstName, String lastName, String phoneNumber) {
        this(email, password, firstName, lastName, phoneNumber,"ROLE_USER");
    }

    public User(String email, String password, String firstName, String lastName, String phoneNumber, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
