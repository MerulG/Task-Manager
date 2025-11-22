package com.project.taskmanager.model;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY) //only load tasks when needed
    @JoinColumn(name = "user_id")
    private User user;
}
