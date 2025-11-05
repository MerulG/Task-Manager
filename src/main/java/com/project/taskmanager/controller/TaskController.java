package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController{
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@RequestBody @Valid TaskRequest taskRequest) {
        return taskService.addTask(taskRequest);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Integer id, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }


}
