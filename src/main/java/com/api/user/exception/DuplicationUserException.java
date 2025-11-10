package com.api.user.exception;

public class DuplicationUserException extends RuntimeException{
    public DuplicationUserException(String message){
        super(message);
    }
}
