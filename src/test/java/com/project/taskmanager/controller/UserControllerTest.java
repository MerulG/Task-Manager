package com.project.taskmanager.controller;

import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) // Load only TaskController & MVC context
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse createUserResponse(Integer id){
        UserResponse response = new UserResponse();
        response.setId(id);
        response.setUsername("SampleUser"+id);
        response.setEmail("sample@email"+id+".com");
        return response;
    }

    @Test
    void shouldReturnUserById() throws Exception {
        //arrange
        UserResponse expectedResponse = createUserResponse(1);
        when(userService.getUser(1)).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(get("/api/users/id/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("SampleUser1"))
                .andExpect(jsonPath("$.email").value("sample@email1.com"));
        verify(userService, times(1)).getUser(1);
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserResponse response1 = createUserResponse(1);
        UserResponse response2 = createUserResponse(2);
        UserResponse response3 = createUserResponse(3);
        Page<UserResponse> pageOfUsers = new PageImpl<>(List.of(response1, response2, response3));
        when(userService.getUsers(anyInt(), anyInt(), anyString())).thenReturn(pageOfUsers);
        //act
        //assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(userService, times(1)).getUsers(anyInt(), anyInt(), anyString());

    }

    @Test
    void shouldDeleteUserById() throws Exception {
        //act
        //assert
        mockMvc.perform(delete("/api/users/{id}",1))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUserById(1);
    }

    @Test
    void shouldAddUserWhenJsonIsValid() throws Exception {
        //arrange
        String validJson = """
        {
            "username": "SampleUser1",
            "email": "sample@email1.com",
            "password": "password"
        }
        """;
        UserResponse expectedResponse = createUserResponse(1);
        when(userService.addUser(any(UserRequest.class))).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("SampleUser1"))
                .andExpect(jsonPath("$.email").value("sample@email1.com"));
        verify(userService,times(1)).addUser(any(UserRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingUserWithNoUsername() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "",
            "email": "sample@email1.com",
            "password": "password"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("username: Username is required"));
        verify(userService,times(0)).addUser(any(UserRequest.class));

    }

    @Test
    void shouldThrowExceptionWhenAddingUserWithNoEmail() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "SampleUser1",
            "email": "",
            "password": "password"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("email: Email is required"));
        verify(userService,times(0)).addUser(any(UserRequest.class));


    }

    @Test
    void shouldThrowExceptionWhenAddingUserWithInvalidEmail() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "SampleUser1",
            "email": "invalidemail",
            "password": "password"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("email: Email is not valid"));
        verify(userService,times(0)).addUser(any(UserRequest.class));

    }

    @Test
    void shouldThrowExceptionWhenAddingUserWithNoPassword() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "SampleUser1",
            "email": "sample@email1.com",
            "password": ""
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("password: Password is required"));
        verify(userService,times(0)).addUser(any(UserRequest.class));


    }

    @Test
    void shouldUpdateUserWhenJsonIsValid() throws Exception {
        //arrange
        String validJson = """
        {
            "username": "SampleUser1",
            "email": "sample@email1.com",
            "password": "password"
        }
        """;
        UserResponse expectedResponse = createUserResponse(1);
        when(userService.updateUser(any(UserRequest.class),anyInt())).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(put("/api/users/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("SampleUser1"))
                .andExpect(jsonPath("$.email").value("sample@email1.com"));
        verify(userService,times(1)).updateUser(any(UserRequest.class),anyInt());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNoUsername() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "",
            "email": "sample@email1.com",
            "password": "password"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/users/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("username: Username is required"));
        verify(userService,times(0)).updateUser(any(UserRequest.class),anyInt());

    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNoEmail() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "SampleUser1",
            "email": "",
            "password": "password"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/users/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("email: Email is required"));
        verify(userService,times(0)).updateUser(any(UserRequest.class),anyInt());

    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserWithInvalidEmail() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "SampleUser1",
            "email": "invalidemail",
            "password": "password"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/users/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("email: Email is not valid"));
        verify(userService,times(0)).updateUser(any(UserRequest.class),anyInt());

    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNoPassword() throws Exception {
        //arrange
        String invalidJson = """
        {
            "username": "SampleUser1",
            "email": "sample@email1.com",
            "password": ""
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/users/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("password: Password is required"));
        verify(userService,times(0)).updateUser(any(UserRequest.class),anyInt());

    }
}
