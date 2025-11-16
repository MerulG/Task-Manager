package com.project.taskmanager.service;

import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnTaskWhenExists() {
        //arrange
        Task task = new Task();
        task.setId(1);
        task.setTitle("test");
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        //act
        TaskResponse result = taskService.getTask(1);

        //assert
        assertEquals(1, result.getId(), "Task ID should match the requested ID");
        assertEquals("test", result.getTitle(), "Task title should match the expected title");
        verify(taskRepository,times(1)).findById(1);
    }

    @Test
    public void shouldThrowTaskNotFoundExceptionWhenNotFound(){
        //arrange
        when(taskRepository.findById(999)).thenReturn(Optional.empty());
        //act
        //assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(999));
        verify(taskRepository,times(1)).findById(999);
    }
}
