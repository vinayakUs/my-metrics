package com.example.authservice.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException
{

    private String message;
    public UserAlreadyExistsException(String message){
        super(message);
        this.message = message;
    }

}
