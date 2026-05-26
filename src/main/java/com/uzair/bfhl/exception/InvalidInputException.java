package com.uzair.bfhl.exception;

/**
 * Custom runtime exception to handle client validation errors gracefully.
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}
