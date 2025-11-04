package com.project.taskmanager.dto;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 20, message = "Description must be at most 255 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
    @NotNull(message = "Priority is required")
    private Priority priority;
    @NotNull(message = "Status is required")
    private Status status;
}
