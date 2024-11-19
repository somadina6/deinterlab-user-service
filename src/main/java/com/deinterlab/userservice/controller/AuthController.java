package com.deinterlab.userservice.controller;

import com.deinterlab.userservice.model.*;
import com.deinterlab.userservice.security.JwtUtil;
import com.deinterlab.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController( UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody User user, BindingResult bindingResult) {
        logger.debug("Authenticating user with email: {}", user.getEmail());
        logger.debug("Authenticating user with password: {}", user.getPassword());

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest()
                    .body(new BaseRestResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid Email/Password", errorMessages));
        }

        AuthResponse authResponse = userService.authenticateUser(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Validated({Default.class, RegisterValidationGroup.class}) User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity
                    .badRequest().body(new BaseRestResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid request", errorMessages));
        }
        logger.debug("Registering user with email: {}", user.getEmail());
        logger.debug("Registering user with password: {}", user.getPassword());
        logger.debug("Registering user with first name: {}", user.getFirstName());
        logger.debug("Registering user with last name: {}", user.getLastName());
        logger.debug("Registering user with phone number: {}", user.getPhoneNumber());

        AuthResponse authResponse = userService.createUser(user);
        return ResponseEntity.ok(authResponse);
    }
}
