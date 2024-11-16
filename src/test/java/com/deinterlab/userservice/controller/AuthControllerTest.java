package com.deinterlab.userservice.controller;


import com.deinterlab.userservice.dto.UserDTO;
import com.deinterlab.userservice.model.AuthResponse;

import com.deinterlab.userservice.model.User;
import com.deinterlab.userservice.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_EMAIL = "somadina6@gmail.com";
    private static final String TEST_PASSWORD = "my-password";
    private static final String TEST_FIRST_NAME = "Somadina";
    private static final String TEST_LAST_NAME = "Eze";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test user registration flow")
    @Order(1)
    void testRegistration() throws Exception {
        // Given
        String registrationPayload = createRegistrationPayload();

        // When
        MvcResult registrationResult = performRegistration(registrationPayload);

        // Then
        verifyUserCreatedInDatabase();
        verifyRegistrationResponse(registrationResult);
    }

    @Test
    @DisplayName("Test user login flow")
    @Order(2)
    void testLogin() throws Exception {
        // Given
        registerUser();
        String loginPayload = createLoginPayload();

        // When
        MvcResult loginResult = performLogin(loginPayload);

        // Then
        verifyLoginResponse(loginResult);
    }

    @Test
    @DisplayName("Test user registration with invalid email")
    @Order(3)
    void testRegistrationWithInvalidEmail() throws Exception {
        // Given
        String registrationPayload = createRegistrationPayload().replace(TEST_EMAIL, "invalid-email");

        // When
        MvcResult registrationResult = performRegistrationWithInvalidEmail(registrationPayload);
        assertEquals(400, registrationResult.getResponse().getStatus(), "Status should be 400");
        assertTrue(registrationResult.getResponse().getContentAsString().contains("Email should be valid"),
                "Response should contain error message");
    }

    @Test
    @DisplayName("Test user login with invalid credentials")
    @Order(4)
    void testLoginWithInvalidCredentials() throws Exception {
        // Given
        registerUser();
        String loginPayload = createLoginPayload().replace(TEST_PASSWORD, "invalid-password");

        // When
        MvcResult loginResult = performLoginWithInvalidCredentials(loginPayload);

        // Then
        assertEquals(401, loginResult.getResponse().getStatus(), "Status should be 401");
        assertTrue(loginResult.getResponse().getContentAsString().contains("Invalid email / password"),
                "Response should contain error message");
    }

    private String createRegistrationPayload() {
        return String.format("""
                {
                    "email": "%s",
                    "password": "%s",
                    "firstName": "%s",
                    "lastName": "%s"
                }""", TEST_EMAIL, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME);
    }

    private String createLoginPayload() {
        return String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }""", TEST_EMAIL, TEST_PASSWORD);
    }

    private MvcResult performRegistration(String payload) throws Exception {
        return mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();
    }

    // performRegistration with invalid email
    private MvcResult performRegistrationWithInvalidEmail(String payload) throws Exception {
        return mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private MvcResult performLogin(String payload) throws Exception {
        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();
    }

    private MvcResult performLoginWithInvalidCredentials(String payload) throws Exception {
        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private void verifyUserCreatedInDatabase() {
        Optional<User> userByEmail = userRepository.findUserByEmail(TEST_EMAIL);
        assertTrue(userByEmail.isPresent(), "User should exist in database");
        User user = userByEmail.get();
        assertEquals(TEST_FIRST_NAME, user.getFirstName(), "First name should match");
        assertEquals(TEST_LAST_NAME, user.getLastName(), "Last name should match");
    }

    private void verifyRegistrationResponse(MvcResult mvcResult) throws Exception {
        AuthResponse response = parseResponse(mvcResult);
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatus(), "Status should be 200");
        assertEquals("User created successfully", response.getMessage(), "Message should indicate successful creation");
    }

    private void verifyLoginResponse(MvcResult mvcResult) throws Exception {
        AuthResponse response = parseResponse(mvcResult);
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getToken(), "Token should be present");
        assertEquals(200, response.getStatus(), "Status should be 200");
        assertEquals("User authenticated", response.getMessage(), "Message should indicate successful authentication");
    }

    private AuthResponse parseResponse(MvcResult mvcResult) throws Exception {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AuthResponse.class);
    }

    private void registerUser() throws Exception {
        performRegistration(createRegistrationPayload());
    }

}