package com.mumuca.diet.exception;

public class UniqueMealLogPreferenceException extends RuntimeException {
    public UniqueMealLogPreferenceException(String message) {
        super(message);
    }

    public UniqueMealLogPreferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
