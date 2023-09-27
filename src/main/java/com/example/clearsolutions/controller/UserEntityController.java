package com.example.clearsolutions.controller;

import com.example.clearsolutions.dto.UpdateUserDto;
import com.example.clearsolutions.dto.UpdateUserNameDto;
import com.example.clearsolutions.dto.UserBirthDateRangeDto;
import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.UserEntity;
import com.example.clearsolutions.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.clearsolutions.controller.EndPoints.*;

@RestController
@RequestMapping(USER)
public class UserEntityController {
    private final UserEntityService userEntityService;

    public UserEntityController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @GetMapping(USER_FIND_BY_ID)
    public ResponseEntity<UserEntity> getUserById(@PathVariable int userId){
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.findUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.createUser(userDto));
    }

    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto){
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.updateUser(updateUserDto));
    }

    @PutMapping(USER_UPDATE_NAME)
    public ResponseEntity<UserEntity> updateUserName(@Valid @RequestBody UpdateUserNameDto updateUserNameDto){
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.updateUserName(updateUserNameDto));
    }

    @DeleteMapping(USER_FIND_BY_ID)
    public ResponseEntity<String> deleteUser(@Valid @PathVariable int userId){
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.deleteUserByUserId(userId));
    }

    @GetMapping(USER_FILTER_BY_BIRTHDATE)
    public ResponseEntity<List<UserEntity>> filterUser(@Valid @RequestBody UserBirthDateRangeDto userBirthDateRangeDto){
        return ResponseEntity.status(HttpStatus.OK).body(userEntityService.searchForUsersByBirthDateRange(userBirthDateRangeDto));
    }

}
