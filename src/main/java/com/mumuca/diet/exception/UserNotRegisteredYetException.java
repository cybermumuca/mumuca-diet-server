package com.mumuca.diet.exception;

public class UserNotRegisteredYetException extends RuntimeException {
    public UserNotRegisteredYetException(String message) {
        super(message);
    }
}
