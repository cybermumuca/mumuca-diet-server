package com.mumuca.diet.exception;

public class CredentialsMismatchException extends RuntimeException {
    public CredentialsMismatchException(String message) {
        super(message);
    }

    public CredentialsMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
