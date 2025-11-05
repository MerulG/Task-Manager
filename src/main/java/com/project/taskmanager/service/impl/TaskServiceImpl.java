package com.project.taskmanager.service.impl;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private Integer nextId = 6;

    List<Task> tasks = new ArrayList<>(List.of(
            new Task(1, "title1", "description1", Priority.MEDIUM, Status.NOT_STARTED),
            new Task(2, "title2", "description2", Priority.LOW, Status.NOT_STARTED),
            new Task(3, "title3", "description3", Priority.MEDIUM, Status.IN_PROGRESS),
            new Task(4, "title4", "description4", Priority.HIGH, Status.COMPLETED),
            new Task(5, "title5", "description5", Priority.VERY_HIGH, Status.NOT_STARTED)
    ));

    private Task createTaskFromRequest(TaskRequest taskRequest){
        return new Task(
                nextId++,
                taskRequest.getTitle(),
                taskRequest.getDescription(),
                taskRequest.getPriority(),
                taskRequest.getStatus()
        );
    }

    private TaskResponse createTaskResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus()
        );
    }

    private Task findTaskById(Integer id){
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @Override
    public TaskResponse getTask(Integer id) {
        return createTaskResponse(findTaskById(id));
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks) {
            taskResponses.add(createTaskResponse(task));
        }
        return taskResponses;
    }

    @Override
    public void deleteTaskById(Integer id) {
        Task task = findTaskById(id);
        tasks.remove(task);
    }

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        Task task = createTaskFromRequest(taskRequest);
        tasks.add(task);
        return createTaskResponse(task);
    }

    @Override
    public TaskResponse updateTask(Integer id, TaskRequest taskRequest) {
        Task existingTask = findTaskById(id);
        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setStatus(taskRequest.getStatus());
        return createTaskResponse(existingTask);
    }
}
