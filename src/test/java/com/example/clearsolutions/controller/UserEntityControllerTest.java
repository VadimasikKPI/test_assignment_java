package com.example.clearsolutions.controller;

import com.example.clearsolutions.dto.UpdateUserDto;
import com.example.clearsolutions.dto.UpdateUserNameDto;
import com.example.clearsolutions.dto.UserBirthDateRangeDto;
import com.example.clearsolutions.dto.UserDto;
import com.example.clearsolutions.entity.UserEntity;
import com.example.clearsolutions.exception.EmailException;
import com.example.clearsolutions.exception.UserAgeRestrictException;
import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.service.UserEntityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserEntityController.class)
@AutoConfigureMockMvc
public class UserEntityControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserEntityService userEntityService;
    private UserEntity user, newUser;
    private String userDto;

    @Before
    public void init(){
        user = new UserEntity(1,"test","Test","test@gmail.com","address",
                "380000000000", LocalDate.of(2003,2,22));
        userDto = "{\n"
                + "  \"firstName\": \"test\",\n"
                + "  \"lastName\": \"Test\",\n"
                + "  \"email\": \"test@gmail.com\",\n"
                + "  \"address\": \"address\",\n"
                + "  \"phoneNumber\": \"380000000000\",\n"
                + "  \"birthday\": \"2002-02-22\"\n"
                + "}";
        newUser = new UserEntity(2,"test2","Test2","test2@gmail.com","address2",
                "380002000000", LocalDate.of(2003,2,22));
    }
    @Test
    public void whenGetUserById_thenReturnJsonUser()throws Exception{
        Mockito.when(userEntityService.findUserById(any(Integer.class))).thenReturn(user);
        mvc.perform(get("/user/{userId}","1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertEquals(true, result.getResponse().getContentAsString()
                        .contains("\"firstName\":\"test\"")));
    }
    @Test
    public void whenGetNotExistUserById_thenReturnErrorMessageAndStatusCode()throws Exception{
        Mockito.when(userEntityService.findUserById(any(Integer.class))).thenThrow(new UserNotFoundException("User with id - 1 not found"));
        mvc.perform(get("/user/{userId}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        Assertions.assertEquals(true, result.getResponse().getContentAsString()
                                .contains("User with id - 1 not found")));
    }

    @Test
    public void whenCreateUser_thenReturnJsonUserAndStatusCreated() throws Exception{
        Mockito.when(userEntityService.createUser(any(UserDto.class))).thenReturn(user);
        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(userDto))
                .andExpect(status().isCreated())
                .andExpect(result -> Assertions.assertEquals(true, result.getResponse().getContentAsString()
                        .contains("\"firstName\":\"test\"")));
    }
    @Test
    public void whenCreateUserWithAgeLowerThen18_thenReturnErrorMessageAndStatusCode() throws Exception{
        Mockito.when(userEntityService.createUser(any(UserDto.class))).thenThrow(new UserAgeRestrictException("User is not 18 years old"));
        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDto))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        Assertions.assertEquals(true, result.getResponse().getContentAsString()
                                .contains("User is not 18 years old")));
    }
    @Test
    public void whenCreateUserWithNotCorrectEmail_thenReturnErrorMessageAndStatusCode() throws Exception{
        Mockito.when(userEntityService.createUser(any(UserDto.class))).thenThrow(new EmailException("Email is not valid, please provide correct email"));
        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDto))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        Assertions.assertEquals(true, result.getResponse().getContentAsString()
                                .contains("Email is not valid, please provide correct email")));
    }

    @Test
    public void whenUpdateUser_thenReturnJsonUserAndStatusCodeOk() throws Exception{
        Mockito.when(userEntityService.updateUser(any(UpdateUserDto.class))).thenReturn(user);
        mvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"1\","+
                                "\"firstName\": \"test\",\n"
                                + "  \"lastName\": \"Test\",\n"
                                + "  \"email\": \"test@gmail.com\",\n"
                                + "  \"address\": \"address\",\n"
                                + "  \"phoneNumber\": \"380000000000\",\n"
                                + "  \"birthday\": \"2002-02-22\"\n"
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertEquals(true, result.getResponse().getContentAsString()
                        .contains("\"firstName\":\"test\"")));
    }
    @Test
    public void whenUpdateUserWithNotCorrectData_thenReturnErrorMessageAndStatusCode() throws Exception{
        Mockito.when(userEntityService.updateUser(any(UpdateUserDto.class)))
                .thenThrow(new UserNotFoundException("User with id - " + 1 + " not found"));
        mvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"1\","+
                                "\"firstName\": \"test\",\n"
                                + "  \"lastName\": \"Test\",\n"
                                + "  \"email\": \"test@gmail.com\",\n"
                                + "  \"address\": \"address\",\n"
                                + "  \"phoneNumber\": \"380000000000\",\n"
                                + "  \"birthday\": \"2002-02-22\"\n"
                                + "}"))
                .andExpect(result ->
                Assertions.assertEquals(true, result.getResponse().getContentAsString()
                        .contains("User with id - 1 not found")));
    }

    @Test
    public void whenUpdateUserName_thenReturnJsonUserAndStatusCodeOk() throws Exception{
        Mockito.when(userEntityService.updateUserName(any(UpdateUserNameDto.class))).thenReturn(user);
        mvc.perform(put("/user/update-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"test\"," +
                                "\"lastName\": \"test\"," +
                                "\"userId\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertEquals(true, result.getResponse().getContentAsString()
                        .contains("\"firstName\":\"test\"")));
    }
    @Test
    public void whenUpdateUserNameWithNotCorrectData_thenReturnErrorStatusCode() throws Exception{
        Mockito.when(userEntityService.updateUserName(any(UpdateUserNameDto.class)))
                .thenThrow(new UserNotFoundException("User with id - " + 1 + " not found"));
        mvc.perform(put("/user/update-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"test\"," +
                                "\"lastName\": \"test\"," +
                                "\"userId\":\"1\"}"))
                .andExpect(result ->
                    Assertions.assertEquals(true, result.getResponse().getContentAsString().contains("User with id - 1 not found")));
    }

    @Test
    public void whenDeleteUserWithExistUser_thenReturnMessageAndStatusCodeOk() throws Exception{
        Mockito.when(userEntityService.deleteUserByUserId(any(Integer.class))).thenReturn("User deleted successfully");
        mvc.perform(delete("/user/{userId}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertEquals("User deleted successfully",result.getResponse().getContentAsString()));
    }
    @Test
    public void whenDeleteUserWithNotExistUser_thenReturnErrorAndBadRequest() throws Exception{
        Mockito.when(userEntityService.deleteUserByUserId(1)).thenThrow(new UserNotFoundException("User with id - " + 1 + " not found"));
        mvc.perform(delete("/user/{userId}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals(true, result.getResponse().getContentAsString().contains("User with id - 1 not found")));
    }

    @Test
    public void whenGetUsersFilteredByDatesOfBirthday_thenReturnStatusCodeOk() throws Exception{
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        users.add(newUser);
        Mockito.when(userEntityService.searchForUsersByBirthDateRange(any(UserBirthDateRangeDto.class))).thenReturn(users);
        mvc.perform(get("/user/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\": \"2002-05-12\"," +
                                "\"endDate\": \"2005-02-20\"}"))

                .andExpect(result -> Assertions.assertEquals("[{\"id\":1,\"firstName\":\"test\",\"lastName\":\"Test\",\"email\":\"test@gmail.com\",\"address\":\"address\",\"phoneNumber\":\"380000000000\",\"birthday\":\"2003-02-22\"}," +
                        "{\"id\":2,\"firstName\":\"test2\",\"lastName\":\"Test2\",\"email\":\"test2@gmail.com\",\"address\":\"address2\",\"phoneNumber\":\"380002000000\",\"birthday\":\"2003-02-22\"}]",
                        result.getResponse().getContentAsString()));
    }
}
