package com.api.user.exception;

public class LoginValidationException extends RuntimeException {
    public LoginValidationException(String message) {
        super(message);
    }
}
