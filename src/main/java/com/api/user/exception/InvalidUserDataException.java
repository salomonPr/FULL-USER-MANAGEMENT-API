package com.api.user.exception;

public class InvalidUserDataException extends RuntimeException{
    public InvalidUserDataException(String message){
        super(message);
    }
}
