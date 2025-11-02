package com.project.taskmanager.model;

import lombok.*;

@Data
@AllArgsConstructor
public class Task {
    private Integer id;
    private String title;
    private String description;
    private String priority;
    private String status;
}
