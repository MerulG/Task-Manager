package com.project.taskmanager.model;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
}
