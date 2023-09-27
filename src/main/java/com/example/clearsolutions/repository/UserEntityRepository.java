package com.example.clearsolutions.repository;

import com.example.clearsolutions.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class UserEntityRepository {
    private List<UserEntity> users;

    public UserEntityRepository() {
        this.users = new ArrayList<>();
    }

    public UserEntityRepository(List<UserEntity> users) {
        this.users = users;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
    public void addUser(UserEntity user){
        this.users.add(user);
    }
    public void removeUser(UserEntity user){
        this.users.remove(user);
    }
    public UserEntity findById(int userId){
        for(UserEntity user: users){
            if(user.getId()==userId){
                return user;
            }
        }
        return null;
    }
}
