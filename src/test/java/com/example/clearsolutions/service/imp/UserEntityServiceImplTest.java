package com.example.clearsolutions.service.imp;

import com.example.clearsolutions.controller.UserEntityController;
import com.example.clearsolutions.dto.UpdateUserDto;
import com.example.clearsolutions.dto.UpdateUserNameDto;
import com.example.clearsolutions.dto.UserBirthDateRangeDto;
import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.UserEntity;
import com.example.clearsolutions.exception.EmailException;
import com.example.clearsolutions.exception.UserAgeRestrictException;
import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.repository.UserEntityRepository;
import com.example.clearsolutions.service.UserEntityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserEntityController.class)
@AutoConfigureMockMvc
public class UserEntityServiceImplTest {
    @Mock
    private UserEntityRepository userEntityRepository;
    private UserDto userDto;
    private UserEntity user, updateUser;
    @MockBean
    private UserEntityService userService;

    @Before
    public void init(){
        userDto = new UserDto("test","Test","test@gmail.com","address",
                "380000000000", LocalDate.of(2003,2,22));
        user = new UserEntity("test","Test","test@gmail.com","address",
                "380000000000", LocalDate.of(2003,2,22));
        updateUser = new UserEntity(10, "test1","Test1","test1@gmail.com","addres1s",
                "380100000000", LocalDate.of(2001,2,22));
    }

    @Test
    public void whenCreateUser_thenReturnUserEntity(){
        when(userService.createUser(any(UserDto.class))).thenReturn(user);
        UserEntity createdUser = userService.createUser(userDto);
        Assertions.assertEquals(createdUser.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(createdUser.getLastName(), userDto.getLastName());
        Assertions.assertEquals(createdUser.getEmail(), userDto.getEmail());
        Assertions.assertEquals(createdUser.getAddress(), userDto.getAddress());
        Assertions.assertEquals(createdUser.getPhoneNumber(), userDto.getPhoneNumber());
        Assertions.assertEquals(createdUser.getBirthday(), userDto.getBirthday());
    }
    @Test
    public void whenCreateUserWithAgeLowerThen18_thenReturnError(){
        when(userService.createUser(any(UserDto.class))).thenThrow(new UserAgeRestrictException("User is not 18 years old"));
        Assertions.assertThrows(UserAgeRestrictException.class, ()->userService.createUser(userDto));
    }
    @Test
    public void whenCreateUserWithNotCorrectEmail_thenReturnError(){
        when(userService.createUser(any(UserDto.class))).thenThrow(new EmailException("Email is not valid, please provide correct email"));
        Assertions.assertThrows(EmailException.class, ()->userService.createUser(userDto));
    }
    @Test
    public void whenGetUserById_thenReturnUserEntity(){
        when(userService.findUserById(any(Integer.class))).thenReturn(user);
        UserEntity userEntity = userService.findUserById(1);
        Assertions.assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), userDto.getLastName());
        Assertions.assertEquals(userEntity.getEmail(), userDto.getEmail());
        Assertions.assertEquals(userEntity.getAddress(), userDto.getAddress());
        Assertions.assertEquals(userEntity.getPhoneNumber(), userDto.getPhoneNumber());
        Assertions.assertEquals(userEntity.getBirthday(), userDto.getBirthday());
    }
    @Test
    public void whenGetNotExistUserById_thenReturnError(){
        when(userService.findUserById(any(Integer.class))).thenThrow(new UserNotFoundException("User with id - 1 not found"));
        Assertions.assertThrows(UserNotFoundException.class, ()->userService.findUserById(1));
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser(){
        when(userService.updateUser(any(UpdateUserDto.class))).thenReturn(updateUser);
        UpdateUserDto updateUserDto = new UpdateUserDto(1, "test1","Test1","test1@gmail.com","addres1s",
               "380100000000", LocalDate.of(2001,2,22));
        UserEntity updatedUser = userService.updateUser(updateUserDto);
        Assertions.assertEquals(updatedUser.getId(), updateUser.getId());
        Assertions.assertEquals(updatedUser.getFirstName(), updateUser.getFirstName());
        Assertions.assertEquals(updatedUser.getLastName(), updateUser.getLastName());
        Assertions.assertEquals(updatedUser.getEmail(), updateUser.getEmail());
        Assertions.assertEquals(updatedUser.getAddress(), updateUser.getAddress());
        Assertions.assertEquals(updatedUser.getPhoneNumber(), updateUser.getPhoneNumber());
        Assertions.assertEquals(updatedUser.getBirthday(), updateUser.getBirthday());

    }
    @Test
    public void whenUpdateUserWithNotCorrectData_thenReturnError() {
        when(userService.updateUser(any(UpdateUserDto.class)))
                .thenThrow(new UserNotFoundException("User with id - " + 1 + " not found"));
        Assertions.assertThrows(UserNotFoundException.class, ()->userService.updateUser(new UpdateUserDto()));
    }

    @Test
    public void whenUpdateUserName_thenReturnUpdatedUser() {
        when(userService.updateUserName(any(UpdateUserNameDto.class))).thenReturn(user);
        UserEntity updatedUser = userService.updateUserName(new UpdateUserNameDto(1,"test25","Test25"));
        Assertions.assertEquals(updatedUser.getId(), user.getId());
        Assertions.assertEquals(updatedUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(updatedUser.getLastName(), user.getLastName());
        Assertions.assertEquals(updatedUser.getEmail(), user.getEmail());
        Assertions.assertEquals(updatedUser.getAddress(), user.getAddress());
        Assertions.assertEquals(updatedUser.getPhoneNumber(), user.getPhoneNumber());
        Assertions.assertEquals(updatedUser.getBirthday(), user.getBirthday());
    }
    @Test
    public void whenUpdateUserNameWithNotCorrectData_thenReturnErrorStatusCode(){
        when(userService.updateUserName(any(UpdateUserNameDto.class)))
                .thenThrow(new UserNotFoundException("User with id - " + 1 + " not found"));
        Assertions.assertThrows(UserNotFoundException.class, ()->userService.updateUserName(new UpdateUserNameDto(1, "test", "Test")));
    }

    @Test
    public void whenDeleteUserWithExistUser_thenReturnMessage(){
        when(userService.deleteUserByUserId(any(Integer.class))).thenReturn("User deleted successfully");
        String message = userService.deleteUserByUserId(1);
        Assertions.assertEquals(message, "User deleted successfully");
    }
    @Test
    public void whenDeleteUserWithNotExistUser_thenReturnErrorAndBadRequest(){
        when(userService.deleteUserByUserId(1)).thenThrow(new UserNotFoundException("User with id - " + 1 + " not found"));
        Assertions.assertThrows(UserNotFoundException.class, ()->userService.deleteUserByUserId(1));
    }

    @Test
    public void whenGetUsersFilteredByDatesOfBirthday_thenReturnListOfUsers(){
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        users.add(updateUser);
        when(userService.searchForUsersByBirthDateRange(any(UserBirthDateRangeDto.class))).thenReturn(users);
       List<UserEntity> filteredUsers = userService.searchForUsersByBirthDateRange(new UserBirthDateRangeDto(
               LocalDate.of(2002,2,22), LocalDate.of(2005,2,23)));
       Assertions.assertEquals(filteredUsers, users);
    }
    @Test
    public void whenGetUsersFilteredByDatesOfBirthday_thenReturnEmptyList(){
        List<UserEntity> users = new ArrayList<>();
        when(userService.searchForUsersByBirthDateRange(any(UserBirthDateRangeDto.class))).thenReturn(Collections.emptyList());
        List<UserEntity> filteredUsers = userService.searchForUsersByBirthDateRange(new UserBirthDateRangeDto(
                LocalDate.of(2002,2,22), LocalDate.of(2005,2,23)));
        Assertions.assertEquals(filteredUsers, users);
    }
}
