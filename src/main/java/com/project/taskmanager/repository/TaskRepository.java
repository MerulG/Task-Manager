package com.project.taskmanager.repository;

import com.project.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

//Spring generates SQL and persistence logic automatically.
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
