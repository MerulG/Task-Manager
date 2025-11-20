package com.project.taskmanager.service;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.impl.TaskServiceImpl;
import com.project.taskmanager.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserUtils userUtils;


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

    @Test
    public void shouldDeleteTask(){
        //arrange
        Task task = new Task();
        task.setId(1);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        //act
        taskService.deleteTaskById(1);
        //assert
        verify(taskRepository,times(1)).delete(task);
    }

    @Test
    public void shouldCreateNewTask(){
        //arrange
        TaskRequest request= new TaskRequest();
        request.setTitle("test");
        User user = new User();
        user.setId(1);
        Task task = new Task();
        task.setId(1);
        task.setUser(user);
        task.setTitle("test");
        when(userUtils.findUserById(1)).thenReturn(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        //act
        TaskResponse response = taskService.addTask(request,1);
        //assert
        assertEquals(1, response.getId(),"Task ID should match the requested ID");
        assertEquals("test", response.getTitle(),"Task title should match the expected title");
        verify(taskRepository,times(1)).save(any(Task.class));
    }

    @Test
    public void shouldUpdateTestWithCorrectFields(){
        //arrange
        TaskRequest request= new TaskRequest();
        request.setTitle("test2");
        request.setDescription("test desc");
        request.setStatus(Status.IN_PROGRESS);
        request.setPriority(Priority.MEDIUM);
        Task task = new Task();
        task.setId(1);
        task.setTitle("test");
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        //get first argument passed to save method
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //act
        TaskResponse response = taskService.updateTask(1,request);
        //assert
        assertEquals(1, response.getId(),"Task ID should match the requested ID");
        assertEquals("test2", response.getTitle(),"Task title should match the expected title");
        assertEquals("test desc", response.getDescription(),"Task description should match the expected description");
        assertEquals(Status.IN_PROGRESS, response.getStatus(),"Task status should match the expected status");
        assertEquals(Priority.MEDIUM, response.getPriority(),"Task priority should match the expected priority");
        verify(taskRepository,times(1)).save(any(Task.class));
    }

    @Test
    public void shouldReturnTasksByUserId(){
        //arrange
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("test1");
        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("test2");
        User user = new User();
        user.setId(1);
        task1.setUser(user);
        task2.setUser(user);
        Page<Task> pageOfTasks = new PageImpl<>(List.of(task1, task2));
        when(taskRepository.findByUserId(eq(1),any(Pageable.class))).thenReturn(pageOfTasks);
        //act
        Page<TaskResponse> tasks = taskService.getTasksByUserId(1, 5, "id", 1);
        //assert
        assertEquals(2,tasks.getTotalElements());
        assertEquals(2,tasks.getNumberOfElements());
        assertEquals("test1",tasks.getContent().get(0).getTitle());
        assertEquals("test2",tasks.getContent().get(1).getTitle());
        verify(taskRepository,times(1)).findByUserId(eq(1),any(Pageable.class));
    }

    @Test
    public void shouldReturnTasksByStatus(){
        //arrange
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("test1");
        task1.setStatus(Status.IN_PROGRESS);
        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("test2");
        task2.setStatus(Status.IN_PROGRESS);
        Page<Task> pageOfTasks = new PageImpl<>(List.of(task1, task2));
        when(taskRepository.findByStatus(eq(Status.IN_PROGRESS),any(Pageable.class))).thenReturn(pageOfTasks);
        //act
        Page<TaskResponse> tasks = taskService.getTasksByStatus(1, 5, "id", Status.IN_PROGRESS);
        //assert
        assertEquals(2,tasks.getTotalElements());
        assertEquals(2,tasks.getNumberOfElements());
        assertEquals("test1",tasks.getContent().get(0).getTitle());
        assertEquals("test2",tasks.getContent().get(1).getTitle());
        verify(taskRepository,times(1)).findByStatus(eq(Status.IN_PROGRESS),any(Pageable.class));

    }

    @Test
    public void shouldReturnTaskByIdAndStatus(){
        //arrange
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("test1");
        task1.setStatus(Status.IN_PROGRESS);
        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("test2");
        task2.setStatus(Status.COMPLETED);
        Page<Task> pageOfTasks = new PageImpl<>(List.of(task1));
        when(taskRepository.findByUserIdAndStatus(eq(1),eq(Status.IN_PROGRESS),any(Pageable.class))).thenReturn(pageOfTasks);
        //act
        Page<TaskResponse> tasks = taskService.getTasksByUserIdAndStatus(1, 5, "id", 1, Status.IN_PROGRESS);
        //assert
        assertEquals(1,tasks.getTotalElements());
        assertEquals(1,tasks.getNumberOfElements());
        assertEquals("test1",tasks.getContent().get(0).getTitle());
        verify(taskRepository,times(1)).findByUserIdAndStatus(eq(1),eq(Status.IN_PROGRESS),any(Pageable.class));

    }

    @Test
    public void shouldReturnTaskByTitle(){
        //arrange
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("test1");
        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("test2");
        Page<Task> pageOfTasks = new PageImpl<>(List.of(task2));
        when(taskRepository.findByTitleContainingIgnoreCase(eq("test2"),any(Pageable.class))).thenReturn(pageOfTasks);
        //act
        Page<TaskResponse> tasks = taskService.getTasksByTitle(1, 5, "id", "test2");
        //assert
        assertEquals(1,tasks.getTotalElements());
        assertEquals(1,tasks.getNumberOfElements());
        assertEquals("test2",tasks.getContent().get(0).getTitle());
        verify(taskRepository,times(1)).findByTitleContainingIgnoreCase(eq("test2"),any(Pageable.class));
    }

}
