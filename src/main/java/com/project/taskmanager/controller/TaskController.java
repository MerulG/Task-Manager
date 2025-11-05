package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.service.TaskService;
import com.project.taskmanager.service.impl.TaskServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private Integer nextId = 6;

    private TaskService taskService;

    //use task service to implement the methods - no buisness logic in controller

    private TaskResponse createTaskResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus()
        );
    }

    private Task createTaskFromRequest(TaskRequest taskRequest){
        return new Task(
                nextId++,
                taskRequest.getTitle(),
                taskRequest.getDescription(),
                taskRequest.getPriority(),
                taskRequest.getStatus()
        );
    }

    private Task findTaskById(Integer id){
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks) {
            TaskResponse taskResponse = createTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Integer id) {
        Task taskFound = findTaskById(id);
        return createTaskResponse(taskFound);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Integer id) {
        Task taskFound = findTaskById(id);
        tasks.remove(taskFound);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@RequestBody @Valid TaskRequest taskRequest) {
        Task newTask = createTaskFromRequest(taskRequest);
        tasks.add(newTask);
        return createTaskResponse(newTask);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Integer id, @RequestBody @Valid TaskRequest taskRequest) {
        Task existingTask = findTaskById(id);
        //update task
        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setStatus(taskRequest.getStatus());
        return createTaskResponse(existingTask);
    }


}
