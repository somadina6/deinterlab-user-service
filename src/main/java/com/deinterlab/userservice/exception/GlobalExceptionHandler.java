package com.deinterlab.userservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

/**
 * Global exception handler for the application
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    private static final String ERROR_MESSAGE = "An error occurred";

    private final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());


    /**
     * Handle all other exceptions
     *
     * @param ex exception
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        LOG.severe(ex.getClass().getName() + " : " + ex.getMessage() + " : " + HttpStatus.INTERNAL_SERVER_ERROR.value());
        LOG.severe("Stack trace: " + ex);
        ErrorResponse response = new ErrorResponse(ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Handle Username Exceptions -
     *
     * @param ex exception
     * @return response entity
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
        LOG.severe(ex.getClass().getName() + " : " + ex.getMessage() + " : " + ex.getStatus());
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(response, ex.getStatus());
    }

}
