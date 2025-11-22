package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController{
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("{id}")
    public TaskResponse getTask(@PathVariable Integer id) {
        return taskService.getTask(id);
    }

    @GetMapping()
    public Page<TaskResponse> getTasks(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer numTasks,
                                       @RequestParam(defaultValue = "id,asc") String sort){
        return taskService.getTasks(page,numTasks,sort);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@PathVariable Integer userId, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.addTask(taskRequest, userId);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Integer id, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.updateTask(taskRequest,id);
    }

    @GetMapping("/user/{userId}")
    public Page<TaskResponse> getTasksByUserId(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer numTasks,
                                               @RequestParam(defaultValue = "id,asc") String sort,
                                               @PathVariable Integer userId) {
        return taskService.getTasksByUserId(page,numTasks,sort,userId);
    }

    @GetMapping("/status/{status}")
    public Page<TaskResponse> getTasksByStatus(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer numTasks,
                                               @RequestParam(defaultValue = "id,asc") String sort,
                                               @PathVariable Status status) {
        return taskService.getTasksByStatus(page,numTasks,sort,status);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public Page<TaskResponse> getTasksByUserIdAndStatus(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer numTasks,
                                                        @RequestParam(defaultValue = "id,asc") String sort,
                                                        @PathVariable Integer userId,
                                                        @PathVariable Status status) {
        return taskService.getTasksByUserIdAndStatus(page,numTasks,sort,userId, status);
    }

    @GetMapping("/search")
    public Page<TaskResponse> getTasksByTitle(@RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer numTasks,
                                              @RequestParam(defaultValue = "id,asc") String sort,
                                              @RequestParam String title) {
        return taskService.getTasksByTitle(page,numTasks,sort,title);
    }


}
