package com.example.clearsolutions.service.imp;

import com.example.clearsolutions.dto.UpdateUserDto;
import com.example.clearsolutions.dto.UpdateUserNameDto;
import com.example.clearsolutions.dto.UserBirthDateRangeDto;
import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.UserEntity;
import com.example.clearsolutions.exception.BirthDateRangeException;
import com.example.clearsolutions.exception.EmailException;
import com.example.clearsolutions.exception.UserAgeRestrictException;
import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.repository.UserEntityRepository;
import com.example.clearsolutions.service.UserEntityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    private final UserEntityRepository userEntityRepository;
    private final EmailValidationService emailValidationService;

    @Value("#{new Integer('${user.age_restrict}')}")
    private int ageRestrict;

    public UserEntityServiceImpl(UserEntityRepository userEntityRepository, EmailValidationService emailValidationService) {
        this.userEntityRepository = userEntityRepository;
        this.emailValidationService = emailValidationService;
    }

    @Override
    public UserEntity createUser(UserDto userDto) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(userDto.getBirthday(), today);
        if(period.getYears()<ageRestrict){
            throw new UserAgeRestrictException("User is not 18 years old");
        }
        if(!emailValidationService.isEmailCorrect(userDto.getEmail())){
            throw new EmailException("Email is not valid, please provide correct email");
        }
        UserEntity userEntity = new UserEntity(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getAddress(),
                userDto.getPhoneNumber(),
                userDto.getBirthday());
        userEntityRepository.addUser(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UpdateUserDto updateUserDto) {
        if(userEntityRepository.findById(updateUserDto.getUserId())==null){
            throw new UserNotFoundException("User with id - " + updateUserDto.getUserId() + " not found");
        }
        UserEntity oldUser = userEntityRepository.findById(updateUserDto.getUserId());
        UserEntity newUser = new UserEntity(
                oldUser.getId(),
                updateUserDto.getFirstName(),
                updateUserDto.getLastName(),
                updateUserDto.getEmail(),
                updateUserDto.getAddress(),
                updateUserDto.getPhoneNumber(),
                updateUserDto.getBirthday());
        userEntityRepository.removeUser(userEntityRepository.findById(oldUser.getId()));
        userEntityRepository.addUser(newUser);
        return newUser;
    }

    @Override
    public UserEntity updateUserNameByUserId(UpdateUserNameDto updateUserNameDto) {
        if(userEntityRepository.findById(updateUserNameDto.getUserId())==null){
            throw new UserNotFoundException("User with id - " + updateUserNameDto.getUserId() + " not found");
        }
        UserEntity oldUser = userEntityRepository.findById(updateUserNameDto.getUserId());
        UserEntity newUser = new UserEntity(
                oldUser.getId(),
                updateUserNameDto.getFirstName(),
                updateUserNameDto.getLastName(),
                oldUser.getEmail(),
                oldUser.getAddress(),
                oldUser.getPhoneNumber(),
                oldUser.getBirthday());
        userEntityRepository.removeUser(userEntityRepository.findById(oldUser.getId()));
        userEntityRepository.addUser(newUser);
        return newUser;
    }

    @Override
    public String deleteUserByUserId(int userId) {
        if(userEntityRepository.findById(userId)==null){
            throw new UserNotFoundException("User with id - " + userId + " not found");
        }
        userEntityRepository.removeUser(userEntityRepository.findById(userId));
        return "User deleted successfully";
    }

    @Override
    public List<UserEntity> searchForUsersByBirthDateRange(UserBirthDateRangeDto userBirthDateRangeDto) {
        if(userBirthDateRangeDto.getEndDate().isBefore(userBirthDateRangeDto.getStartDate())){
            throw new BirthDateRangeException("End date can not be earlier then start date");
        }
        List<UserEntity> filteredUsers = new ArrayList<>();
        for(UserEntity user: userEntityRepository.getUsers()){
            if(user.getBirthday().isAfter(userBirthDateRangeDto.getStartDate()) && user.getBirthday().isBefore(userBirthDateRangeDto.getEndDate())){
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    @Override
    public UserEntity findUserById(int userId) {
        if(userEntityRepository.findById(userId)==null){
            throw new UserNotFoundException("User with id - " + userId + " not found");
        }
        return userEntityRepository.findById(userId);
    }
}
