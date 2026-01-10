package com.project.taskmanager.exception;

import com.project.taskmanager.controller.TaskController;
import com.project.taskmanager.security.JwtProvider;
import com.project.taskmanager.service.TaskService;
import com.project.taskmanager.service.impl.ApiUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.access.AccessDeniedException;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TaskController.class)
@WithMockUser(username = "admin", roles = "ADMIN")
public class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @MockBean
    JwtProvider jwtProvider;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    ApiUserDetailsService apiUserDetailsService;

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception{
        //arrange
        when(taskService.getTask(999)).thenThrow(new TaskNotFoundException("Task not found"));
        //act
        //assert
        mockMvc.perform(get("/api/tasks/{id}",999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.messages[0]").value("Task not found"))
                .andExpect(jsonPath("$.path").value("/api/tasks/999"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception{
        //arrange
        when(taskService.getTasksByUserId(anyInt(),anyInt(),anyString(),eq(999))).thenThrow(new UserNotFoundException("User not found"));
        //act
        //assert
        mockMvc.perform(get("/api/tasks/user/{userid}",999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.messages[0]").value("User not found"))
                .andExpect(jsonPath("$.path").value("/api/tasks/user/999"));
    }

    @Test
    void shouldReturnBadRequestWhenGetTaskWithInvalidArgument() throws Exception{
        //arrange
        when(taskService.getTask(anyInt())).thenThrow(new IllegalArgumentException("Invalid value for request parameter"));
        //act
        //assert
        mockMvc.perform(get("/api/tasks/{id}",-1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Invalid value for request parameter"))
                .andExpect(jsonPath("$.path").value("/api/tasks/-1"));
    }

    @Test
    void shouldReturnAccessDeniedExceptionWhenUsingProtectedEndpoints() throws Exception{
        //arrange
        when(taskService.getTask(1)).thenThrow(new AccessDeniedException("Access denied."));
        //act
        //assert
        mockMvc.perform(get("/api/tasks/{id}", 1))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.messages[0]").value("Access denied."))
                .andExpect(jsonPath("$.path").value("/api/tasks/1"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedErrorOccurs() throws Exception{
        //arrange
        when(taskService.getTask(1)).thenThrow(new RuntimeException("Unexpected error"));
        //act
        //assert
        mockMvc.perform(get("/api/tasks/{id}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.messages[0]").value("Unexpected error occurred."))
                .andExpect(jsonPath("$.path").value("/api/tasks/1"));
    }

}
