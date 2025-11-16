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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    @Test
    public void shouldReturnAllTasks(){
        //arrange
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("test1");
        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("test2");
        Page<Task> pageOfTasks = new PageImpl<>(List.of(task1, task2));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(pageOfTasks);

        //act
        Page<TaskResponse> tasks = taskService.getTasks(1, 5, "id");

        //assert
        assertEquals(2,tasks.getTotalElements());
        assertEquals(2,tasks.getNumberOfElements());
        assertEquals("test1",tasks.getContent().get(0).getTitle());
        assertEquals("test2",tasks.getContent().get(1).getTitle());
        verify(taskRepository,times(1)).findAll(any(Pageable.class));
    }
}
