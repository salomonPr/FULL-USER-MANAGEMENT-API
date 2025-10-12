package com.api.userManagementApi.exception;

public class DuplicationUserException extends RuntimeException{

    public DuplicationUserException(String message){
        super(message);
    }
}
