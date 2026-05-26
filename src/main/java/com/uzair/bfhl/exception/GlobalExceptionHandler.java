package com.uzair.bfhl.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * Controller advice class providing global exception handling and ensuring
 * the API always returns structured, clean JSON without leaking internal details or stacktraces.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DTO validation failures (e.g. data is null).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("Validation error encountered: {}", details);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .isSuccess(false)
                .message("Validation Failed")
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles malformed or unparseable JSON payloads.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed HTTP request body: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .isSuccess(false)
                .message("Malformed Request Body")
                .details("The request body contains invalid JSON format.")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom business-level invalid input exceptions.
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        log.warn("Business validation error: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .isSuccess(false)
                .message("Invalid Input")
                .details(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all exception handler to intercept server-side errors, prevent application crashes,
     * and present a professional and sanitized JSON format response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Internal server error occurred", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .isSuccess(false)
                .message("Internal Server Error")
                .details("An unexpected error occurred. Please contact the administrator.")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
