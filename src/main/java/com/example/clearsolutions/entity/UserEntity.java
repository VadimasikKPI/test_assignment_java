package com.example.clearsolutions.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.aot.generate.GeneratedMethod;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;


public class UserEntity {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String firstName;
    private String lastName;

    private String email;
    private String address;
    private String phoneNumber;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;

    public UserEntity() {
    }

    public UserEntity(String firstName, String lastName, String email, String address, String phoneNumber, LocalDate birthday) {
        this.id = count.incrementAndGet();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public UserEntity(int id, String firstName, String lastName, String email, String address, String phoneNumber, LocalDate birthday) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
