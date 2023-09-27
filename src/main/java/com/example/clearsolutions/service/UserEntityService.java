package com.example.clearsolutions.service;

import com.example.clearsolutions.dto.UpdateUserDto;
import com.example.clearsolutions.dto.UpdateUserNameDto;
import com.example.clearsolutions.dto.UserBirthDateRangeDto;
import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.UserEntity;

import java.util.List;

public interface UserEntityService {
    UserEntity createUser(UserDto userDto);
    UserEntity updateUser(UpdateUserDto updateUserDto);
    UserEntity updateUserName(UpdateUserNameDto userDto);
    String deleteUserByUserId(int userId);
    List<UserEntity> searchForUsersByBirthDateRange(UserBirthDateRangeDto userBirthDateRangeDto);

    UserEntity findUserById(int userId);

}
