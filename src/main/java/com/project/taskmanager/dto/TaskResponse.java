package com.project.taskmanager.dto;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import lombok.Data;

@Data
public class TaskResponse {
    private Integer id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
}
