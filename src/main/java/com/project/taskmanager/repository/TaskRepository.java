package com.project.taskmanager.repository;

import com.project.taskmanager.enums.Status;
import com.project.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//Spring generates SQL and persistence logic automatically.
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findByUserId(Integer userId, Pageable pageable);
    Page<Task> findByStatus(Status status, Pageable pageable);
    Page<Task> findByUserIdAndStatus(Integer userId, Status status, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
