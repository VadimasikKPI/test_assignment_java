package com.example.clearsolutions.exception;

public class UserAgeRestrictException extends RuntimeException{
    public UserAgeRestrictException(String s) {
        super(s);
    }
}
