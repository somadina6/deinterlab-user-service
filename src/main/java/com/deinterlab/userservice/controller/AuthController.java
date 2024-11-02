package com.deinterlab.userservice.controller;

import com.deinterlab.userservice.model.AuthRequest;
import com.deinterlab.userservice.model.AuthResponse;
import com.deinterlab.userservice.model.BaseRestResponse;
import com.deinterlab.userservice.security.JwtUtil;
import com.deinterlab.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController( UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest()
                    .body(new BaseRestResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid Email/Password", errorMessages));
        }
        AuthResponse authResponse = userService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity
                    .badRequest().body(new BaseRestResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid request", errorMessages));
        }

        AuthResponse authResponse = userService.createUser(authRequest.getEmail(), authRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }
}
