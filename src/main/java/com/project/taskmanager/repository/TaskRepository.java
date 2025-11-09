package com.project.taskmanager.repository;

import com.project.taskmanager.enums.Status;
import com.project.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Spring generates SQL and persistence logic automatically.
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(Integer userId);
    List<Task> findByStatus(Status status);
    List<Task> findByUserIdAndStatus(Integer userId, Status status);
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}
