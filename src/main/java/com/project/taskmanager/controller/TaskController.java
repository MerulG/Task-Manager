package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController{
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("id/{id}")
    public TaskResponse getTask(@PathVariable Integer id) {
        return taskService.getTask(id);
    }

    @GetMapping()
    public List<TaskResponse> getTasks() {
        return taskService.getAllTasks();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

    @PostMapping("user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@PathVariable Integer userId, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.addTask(taskRequest, userId);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Integer id, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    @GetMapping("/user/{userId}")
    public List<TaskResponse> getTasksByUserId(@PathVariable Integer userId) {
        return taskService.getTasksByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public List<TaskResponse> getTasksByStatus(@PathVariable Status status) {
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public List<TaskResponse> getTasksByUserIdAndStatus(@PathVariable Integer userId, @PathVariable Status status) {
        return taskService.getTasksByUserIdAndStatus(userId, status);
    }

    @GetMapping("/search")
    public List<TaskResponse> getTasksByTitle(@RequestParam String title) {
        return taskService.getTasksByTitle(title);
    }


}
