package com.project.taskmanager.service;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse getTask(Integer id);
    List<TaskResponse> getAllTasks();
    void deleteTaskById(Integer id);
    TaskResponse addTask(TaskRequest taskRequest);
    TaskResponse updateTask(Integer id, TaskRequest taskRequest);
}
