package com.project.taskmanager.dto;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    @Schema(description = "The title of the task", example = "Complete Docs")
    @NotBlank(message = "Title is required")
    @Size(max = 20, message = "Title must be at most 20 characters")
    private String title;
    @Schema(description = "Detailed description of the task", example = "Write comprehensive documentation for the Task Manager API including all endpoints and usage examples")
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
    @Schema(description = "Priority level of the task", example = "HIGH")
    @NotNull(message = "Priority is required")
    private Priority priority;
    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    @NotNull(message = "Status is required")
    private Status status;
}
