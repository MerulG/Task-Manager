package com.project.taskmanager.controller;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "Get a task by ID",
            description = "Retrieves a single task by its unique identifier."
    )
    @GetMapping("{id}")
    public TaskResponse getTask(
            @Parameter(description = "The unique identifier of the task") @PathVariable Integer id) {
        return taskService.getTask(id);
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves a paginated list of all tasks with sorting options."
    )
    @GetMapping()
    public Page<TaskResponse> getTasks(
            @Parameter(description = "Page number (zero-indexed)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Number of tasks per page") @RequestParam(defaultValue = "10") Integer numTasks,
            @Parameter(description = "Sort criteria in format: property,(asc|desc)") @RequestParam(defaultValue = "id,asc") String sort){
        return taskService.getTasks(page,numTasks,sort);
    }

    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its unique identifier."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @Parameter(description = "The unique identifier of the task to delete") @PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and assigns it to a specific user."
    )
    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(
            @Parameter(description = "The unique identifier of the user to assign the task to") @PathVariable Integer userId,
            @Parameter(description = "Task data containing title, description, and status") @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.addTask(taskRequest, userId);
    }

    @Operation(
            summary = "Update a task",
            description = "Updates an existing task with new information."
    )
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @Parameter(description = "The unique identifier of the task to update") @PathVariable Integer id,
            @Parameter(description = "Updated task data") @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.updateTask(taskRequest,id);
    }

    @Operation(
            summary = "Get tasks by user ID",
            description = "Retrieves a paginated list of tasks assigned to a specific user."
    )
    @GetMapping("/user/{userId}")
    public Page<TaskResponse> getTasksByUserId(
            @Parameter(description = "Page number (zero-indexed)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Number of tasks per page") @RequestParam(defaultValue = "10") Integer numTasks,
            @Parameter(description = "Sort criteria in format: property,(asc|desc)") @RequestParam(defaultValue = "id,asc") String sort,
            @Parameter(description = "The unique identifier of the user") @PathVariable Integer userId) {
        return taskService.getTasksByUserId(page,numTasks,sort,userId);
    }

    @Operation(
            summary = "Get tasks by status",
            description = "Retrieves a paginated list of tasks filtered by their status."
    )
    @GetMapping("/status/{status}")
    public Page<TaskResponse> getTasksByStatus(
            @Parameter(description = "Page number (zero-indexed)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Number of tasks per page") @RequestParam(defaultValue = "10") Integer numTasks,
            @Parameter(description = "Sort criteria in format: property,(asc|desc)") @RequestParam(defaultValue = "id,asc") String sort,
            @Parameter(description = "The status to filter tasks by") @PathVariable Status status) {
        return taskService.getTasksByStatus(page,numTasks,sort,status);
    }

    @Operation(
            summary = "Get tasks by user ID and status",
            description = "Retrieves a paginated list of tasks for a specific user filtered by status."
    )
    @GetMapping("/user/{userId}/status/{status}")
    public Page<TaskResponse> getTasksByUserIdAndStatus(
            @Parameter(description = "Page number (zero-indexed)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Number of tasks per page") @RequestParam(defaultValue = "10") Integer numTasks,
            @Parameter(description = "Sort criteria in format: property,(asc|desc)") @RequestParam(defaultValue = "id,asc") String sort,
            @Parameter(description = "The unique identifier of the user") @PathVariable Integer userId,
            @Parameter(description = "The status to filter tasks by") @PathVariable Status status) {
        return taskService.getTasksByUserIdAndStatus(page,numTasks,sort,userId, status);
    }

    @Operation(
            summary = "Search tasks by title",
            description = "Retrieves a paginated list of tasks that match the specified title search term."
    )
    @GetMapping("/search")
    public Page<TaskResponse> getTasksByTitle(
            @Parameter(description = "Page number (zero-indexed)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Number of tasks per page") @RequestParam(defaultValue = "10") Integer numTasks,
            @Parameter(description = "Sort criteria in format: property,(asc|desc)") @RequestParam(defaultValue = "id,asc") String sort,
            @Parameter(description = "The title search term to filter tasks by") @RequestParam String title) {
        return taskService.getTasksByTitle(page,numTasks,sort,title);
    }


}
