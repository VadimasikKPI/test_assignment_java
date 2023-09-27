package com.example.clearsolutions.service.imp;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidationService {
    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public boolean isEmailCorrect(String email){
        return Pattern.matches(regex, email);
    }
}
