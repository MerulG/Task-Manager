package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.service.TaskService;
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


@WebMvcTest(TaskController.class) // Load only TaskController & MVC context
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskResponse createTaskResponse(Integer id){
        TaskResponse response = new TaskResponse();
        response.setId(id);
        response.setTitle("Sample Task "+id);
        response.setDescription("Test task description "+id);
        response.setStatus(Status.IN_PROGRESS);
        response.setPriority(Priority.MEDIUM);
        return response;
    }

    @Test
    void shouldReturnTaskById() throws Exception{
        //arrange
        TaskResponse expectedResponse = createTaskResponse(1);
        when(taskService.getTask(1)).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(get("/api/tasks/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task 1"))
                .andExpect(jsonPath("$.description").value("Test task description 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
        verify(taskService,times(1)).getTask(1);
    }

    @Test
    void shouldReturnAllTasks() throws Exception {
        //arrange
        TaskResponse response1 = createTaskResponse(1);
        TaskResponse response2 = createTaskResponse(2);
        TaskResponse response3 = createTaskResponse(3);
        Page<TaskResponse> pageOfTasks = new PageImpl<>(List.of(response1, response2, response3));
        when(taskService.getTasks(anyInt(), anyInt(), anyString())).thenReturn(pageOfTasks);
        //act
        //assert
        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("numTasks", "5")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(taskService, times(1)).getTasks(anyInt(), anyInt(), anyString());
    }

    @Test
    void shouldDeleteTaskById() throws Exception {
        //act
        //assert
        mockMvc.perform(delete("/api/tasks/{id}",1))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTaskById(1);
    }

    @Test
    void addTaskWhenJsonIsValid() throws Exception {
        //arrange
        String validJson = """
        {
            "title": "Sample Task 1",
            "description": "Test task description 1",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        TaskResponse expectedResponse = createTaskResponse(1);
        when(taskService.addTask(any(TaskRequest.class),anyInt())).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task 1"))
                .andExpect(jsonPath("$.description").value("Test task description 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
        verify(taskService,times(1)).addTask(any(TaskRequest.class),eq(1));

    }

    @Test
    void throwExceptionWhenAddingTaskWithNoTitle() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "",
            "description": "Test task description 1",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("title: Title is required"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenAddingTaskWithTitleTooLong() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Title with 21 chars!!",
            "description": "Test task description 1",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("title: Title must be at most 20 " +
                        "characters"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenAddingTaskWithNoDescription() throws Exception{
        //arrange
        String inValidJson = """
        {
            "title": "Sample Task 1",
            "description": "",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inValidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("description: Description is " +
                        "required"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenAddingTaskWithDescriptionTooLong() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Sample Task 1",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor sit amet extra characters added to exceed the limit.",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("description: Description must be " +
                        "at most 255 characters"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenAddingTaskWithNoPriority() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Title 1",
            "description": "Test task description 1",
            "status": "IN_PROGRESS"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("priority: Priority is required"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenAddingTaskWithInvalidPriority() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Sample Task 1",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor sit amet extra characters added to exceed the limit.",
            "status": "IN_PROGRESS",
            "priority": "INVLAID PRIORITY"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Invalid Priority value. Possible values: LOW, MEDIUM, HIGH, VERY_HIGH"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }
    
    @Test
    void throwExceptionWhenAddingTaskWithNoStatus() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Title 1",
            "description": "Test task description 1",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("status: Status is required"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenAddingTaskWithInvalidStatus() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Sample Task 1",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor sit amet extra characters added to exceed the limit.",
            "status": "INVALID_STATUS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(post("/api/tasks/user/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Invalid Status value. Possible values: NOT_STARTED, IN_PROGRESS, COMPLETED"));
        verify(taskService,times(0)).addTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void updateTaskWhenJsonIsValid() throws Exception {
        //arrange
        String validJson = """
        {
            "title": "Sample Task 1",
            "description": "Test task description 1",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        TaskResponse expectedResponse = createTaskResponse(1);
        when(taskService.updateTask(any(TaskRequest.class),anyInt())).thenReturn(expectedResponse);
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task 1"))
                .andExpect(jsonPath("$.description").value("Test task description 1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
        verify(taskService,times(1)).updateTask(any(TaskRequest.class),eq(1));

    }

    @Test
    void throwExceptionWhenUpdatingTaskWithNoTitle() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "",
            "description": "Test task description 1",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("title: Title is required"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithTitleTooLong() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Title with 21 chars!!",
            "description": "Test task description 1",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("title: Title must be at most 20 " +
                        "characters"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithNoDescription() throws Exception{
        //arrange
        String inValidJson = """
        {
            "title": "Sample Task 1",
            "description": "",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inValidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("description: Description is " +
                        "required"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithDescriptionTooLong() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Sample Task 1",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor sit amet extra characters added to exceed the limit.",
            "status": "IN_PROGRESS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("description: Description must be " +
                        "at most 255 characters"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithNoPriority() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Title 1",
            "description": "Test task description 1",
            "status": "IN_PROGRESS"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("priority: Priority is required"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithInvalidPriority() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Sample Task 1",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor sit amet extra characters added to exceed the limit.",
            "status": "IN_PROGRESS",
            "priority": "INVALID PRIORITY"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Invalid Priority value. Possible values: LOW, MEDIUM, HIGH, VERY_HIGH"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithNoStatus() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Title 1",
            "description": "Test task description 1",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("status: Status is required"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void throwExceptionWhenUpdatingTaskWithInvalidStatus() throws Exception{
        //arrange
        String invalidJson = """
        {
            "title": "Sample Task 1",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor sit amet extra characters added to exceed the limit.",
            "status": "INVALID_STATUS",
            "priority": "MEDIUM"
        }
        """;
        //act
        //assert
        mockMvc.perform(put("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Invalid Status value. Possible values: NOT_STARTED, IN_PROGRESS, COMPLETED"));
        verify(taskService,times(0)).updateTask(any(TaskRequest.class),eq(1));
    }

    @Test
    void shouldReturnAllTasksByUserId() throws Exception {
        //arrange
        TaskResponse response1 = createTaskResponse(1);
        TaskResponse response2 = createTaskResponse(2);
        TaskResponse response3 = createTaskResponse(3);
        Page<TaskResponse> pageOfTasks = new PageImpl<>(List.of(response1, response2, response3));
        when(taskService.getTasksByUserId(anyInt(), anyInt(), anyString(),anyInt())).thenReturn(pageOfTasks);
        //act
        //assert
        mockMvc.perform(get("/api/tasks/user/{userId}",1)
                        .param("page", "0")
                        .param("numTasks", "5")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(taskService, times(1)).getTasksByUserId(anyInt(), anyInt(), anyString(),anyInt());
    }

    @Test
    void shouldReturnAllTasksByStatus() throws Exception {
        //arrange
        TaskResponse response1 = createTaskResponse(1);
        TaskResponse response2 = createTaskResponse(2);
        TaskResponse response3 = createTaskResponse(3);
        Page<TaskResponse> pageOfTasks = new PageImpl<>(List.of(response1, response2, response3));
        when(taskService.getTasksByStatus(anyInt(), anyInt(), anyString(),any(Status.class))).thenReturn(pageOfTasks);
        //act
        //assert
        mockMvc.perform(get("/api/tasks/status/{status}","IN_PROGRESS")
                        .param("page", "0")
                        .param("numTasks", "5")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(taskService, times(1)).getTasksByStatus(anyInt(), anyInt(), anyString(), any(Status.class));
    }

    @Test
    void shouldReturnAllTasksByUserIdAndStatus() throws Exception {
        //arrange
        TaskResponse response1 = createTaskResponse(1);
        TaskResponse response2 = createTaskResponse(2);
        TaskResponse response3 = createTaskResponse(3);
        Page<TaskResponse> pageOfTasks = new PageImpl<>(List.of(response1, response2, response3));
        when(taskService.getTasksByUserIdAndStatus(anyInt(), anyInt(), anyString(), anyInt(), any(Status.class))).thenReturn(pageOfTasks);
        //act
        //assert
        mockMvc.perform(get("/api/tasks/user/{userId}/status/{status}", 1, "IN_PROGRESS")
                        .param("page", "0")
                        .param("numTasks", "5")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(taskService, times(1)).getTasksByUserIdAndStatus(anyInt(), anyInt(), anyString(), anyInt(), any(Status.class));
    }

    @Test
    void shouldReturnAllTasksByTitle() throws Exception {
        //arrange
        TaskResponse response1 = createTaskResponse(1);
        TaskResponse response2 = createTaskResponse(2);
        TaskResponse response3 = createTaskResponse(3);
        Page<TaskResponse> pageOfTasks = new PageImpl<>(List.of(response1, response2, response3));
        when(taskService.getTasksByTitle(anyInt(), anyInt(), anyString(),anyString())).thenReturn(pageOfTasks);
        //act
        //assert
        mockMvc.perform(get("/api/tasks/search")
                        .param("page", "0")
                        .param("numTasks", "5")
                        .param("sort", "id,asc")
                        .param("title","sample task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(taskService, times(1)).getTasksByTitle(anyInt(), anyInt(), anyString(), anyString());
    }
}
