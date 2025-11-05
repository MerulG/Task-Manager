package com.project.taskmanager.service;

import com.project.taskmanager.model.Task;

import java.util.List;

public interface TaskService {
    Task getTaskById(Integer id);
    List<Task> getTasks();
    void deleteTaskById(Integer id);
    Task addTask(Integer id, Task task);
    Task updateTask(Integer id, Task task);
}
