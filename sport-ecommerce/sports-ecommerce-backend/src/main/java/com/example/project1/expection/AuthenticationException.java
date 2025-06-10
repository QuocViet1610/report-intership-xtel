package com.example.project1.expection;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String message){
        super(message);
    }

}
