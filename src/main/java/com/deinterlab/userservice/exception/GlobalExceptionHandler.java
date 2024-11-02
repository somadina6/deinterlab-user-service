package com.deinterlab.userservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.logging.Logger;

/**
 * Global exception handler for the application
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    private static final String ERROR_MESSAGE = "An error occurred";

    private final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());

    /**
     * Handle UsernameNotFoundException
     *
     * @param ex exception
     * @return response entity
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        LOG.severe(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle all other exceptions
     *
     * @param ex exception
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        LOG.severe(ex.getMessage());
        ErrorResponse response = new ErrorResponse(ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
