package com.deinterlab.userservice.service;

import com.deinterlab.userservice.exception.UserException;
import com.deinterlab.userservice.model.AuthRequest;
import com.deinterlab.userservice.model.AuthResponse;
import com.deinterlab.userservice.model.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        String email = "somadina600@gmail.com";
        String password = "password";
        String firstName = "Somadina";
        String lastName = "Eze";
        AuthRequest authRequest = new AuthRequest(email, password, firstName, lastName);

        userService.createUser(authRequest);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    void createUser() {
        String email = "somadina6@gmail.com";
        String password = "password";
        String firstName = "Somadina";
        String lastName = "Eze";
        AuthRequest authRequest = new AuthRequest(email, password, firstName, lastName);

        AuthResponse response = userService.createUser(authRequest);
        assertEquals(200, response.getStatus());
        assertEquals("User created successfully", response.getMessage());

        // Check if the user was created
        User userByEmail = userService.getUserByEmail(email);
        assertNotNull(userByEmail);
        assertEquals(email, userByEmail.getEmail());
        assertTrue(userByEmail.getPassword().startsWith("$2a$10$")); // Check if the password is hashed
        assertEquals(firstName, userByEmail.getFirstName());
        assertEquals(lastName, userByEmail.getLastName());
        assertEquals("ROLE_USER", userByEmail.getRole());

        // Test for user to throw an exception if user already exists
        UserException exception = assertThrows(UserException.class, () -> userService.createUser(authRequest));
        assertEquals("User already exists", exception.getMessage());
        assertEquals(400, exception.getStatus().value());
    }

    @Test
    void authenticateUser() {
        String email = "somadina600@gmail.com";
        String password = "password";

        AuthResponse response = userService.authenticateUser(email, password);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getToken());
        assertEquals("User authenticated", response.getMessage());
        assertEquals("Somadina", response.getData().getFirstName());
        assertEquals("Eze", response.getData().getLastName());
        assertEquals("ROLE_USER", response.getData().getRole());

        // Test for user to throw an exception if user does not exist
        UserException exception = assertThrows(UserException.class, () -> userService.authenticateUser("somadina7@gmail.com", password));
        assertEquals("User not found", exception.getMessage());
        assertEquals(502, exception.getStatus().value());

        // Test for user to throw an exception if password is incorrect
        exception = assertThrows(UserException.class, () -> userService.authenticateUser(email, "wrongpassword"));
        assertEquals("Invalid credentials", exception.getMessage());
        assertEquals(401, exception.getStatus().value());

    }
}