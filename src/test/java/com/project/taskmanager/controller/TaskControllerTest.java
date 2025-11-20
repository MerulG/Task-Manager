package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TaskController.class) // Load only TaskController & MVC context
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void shouldReturnTaskById() throws Exception{
        //arrange
        TaskResponse expectedResponse = new TaskResponse();
        expectedResponse.setId(1);
        expectedResponse.setTitle("Sample Task");
        expectedResponse.setDescription("Test task description");
        expectedResponse.setStatus(Status.IN_PROGRESS);
        expectedResponse.setPriority(Priority.MEDIUM);
        when(taskService.getTask(1)).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(get("/api/tasks/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.description").value("Test task description"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
        verify(taskService,times(1)).getTask(1);

    }
}
