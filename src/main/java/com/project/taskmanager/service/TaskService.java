package com.project.taskmanager.service;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;

import java.util.List;

public interface TaskService {
    TaskResponse getTask(Integer id);

    List<TaskResponse> getAllTasks();

    void deleteTaskById(Integer id);

    TaskResponse addTask(TaskRequest taskRequest, Integer userId);

    TaskResponse updateTask(Integer id, TaskRequest taskRequest);

    List<TaskResponse> getTasksByUserId(Integer userId);

    List<TaskResponse> getTasksByStatus(Status status);

    List<TaskResponse> getTasksByUserIdAndStatus(Integer userId, Status status);

    List<TaskResponse> getTasksByTitle(String title);
}
