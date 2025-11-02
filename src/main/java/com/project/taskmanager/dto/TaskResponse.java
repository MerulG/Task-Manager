package com.project.taskmanager.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Integer id;
    private String title;
    private String description;
    private String priority;
    private String status;
}
