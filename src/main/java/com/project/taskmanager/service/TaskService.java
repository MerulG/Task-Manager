package com.project.taskmanager.service;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;
import org.springframework.data.domain.Page;

public interface TaskService {
    TaskResponse getTask(Integer id);
    Page<TaskResponse> getTasks(Integer page, Integer numTasks, String sort);
    void deleteTaskById(Integer id);
    TaskResponse addTask(TaskRequest taskRequest, Integer userId);
    TaskResponse updateTask(TaskRequest taskRequest, Integer userId);
    Page<TaskResponse> getTasksByUserId(Integer page, Integer numTasks, String sort, Integer userId);
    Page<TaskResponse> getTasksByStatus(Integer page, Integer numTasks, String sort, Status status);
    Page<TaskResponse> getTasksByUserIdAndStatus(Integer page, Integer numTasks, String sort, Integer userId, Status status);
    Page<TaskResponse> getTasksByTitle(Integer page, Integer numTasks, String sort, String title);
    boolean isTaskOwner(Integer taskId, Integer userId);
}
