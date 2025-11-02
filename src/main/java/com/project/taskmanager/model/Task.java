package com.project.taskmanager.model;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import lombok.*;

@Data
@AllArgsConstructor
public class Task {
    private Integer id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
}
