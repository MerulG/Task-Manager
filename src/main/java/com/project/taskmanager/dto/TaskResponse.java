package com.project.taskmanager.dto;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskResponse {
    @Schema(description = "The unique identifier of the task", example = "1")
    private Integer id;
    @Schema(description = "The title of the task", example = "Complete project documentation")
    private String title;
    @Schema(description = "Detailed description of the task", example = "Write comprehensive documentation for the Task Manager API including all endpoints and usage examples")
    private String description;
    @Schema(description = "Priority level of the task", example = "HIGH")
    private Priority priority;
    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    private Status status;
}
