package com.project.taskmanager.service.impl;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.TaskService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private Task createTaskFromRequest(TaskRequest taskRequest){
        Task newTask = new Task();
        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setPriority(taskRequest.getPriority());
        newTask.setStatus(taskRequest.getStatus());
        return newTask;
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

    public Task findTaskById(Integer id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isEmpty()) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        return task.get();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTask(Integer id) {
        return createTaskResponse(findTaskById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        List<Task> allTasks = taskRepository.findAll();
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : allTasks) {
            taskResponses.add(createTaskResponse(task));
        }
        return taskResponses;
    }

    @Override
    @Transactional
    public void deleteTaskById(Integer id) {
        taskRepository.delete(findTaskById(id));
    }

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        Task task = createTaskFromRequest(taskRequest);
        task = taskRepository.save(task);
        return createTaskResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Integer id, TaskRequest taskRequest) {
        Task existingTask = findTaskById(id);
        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setStatus(taskRequest.getStatus());
        existingTask = taskRepository.save(existingTask);
        return createTaskResponse(existingTask);
    }
}
