package com.project.taskmanager.service.impl;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.service.TaskService;
import com.project.taskmanager.utils.PaginationUtils;
import com.project.taskmanager.utils.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserUtils userUtils;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "title","description", "status", "priority");

    public TaskServiceImpl(TaskRepository taskRepository, UserUtils userUtils) {
        this.taskRepository = taskRepository;
        this.userUtils = userUtils;
    }

    private Task createTaskFromRequest(TaskRequest taskRequest, User user){
        Task newTask = new Task();
        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setPriority(taskRequest.getPriority());
        newTask.setStatus(taskRequest.getStatus());
        newTask.setUser(user);
        return newTask;
    }

    private TaskResponse createTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setPriority(task.getPriority());
        taskResponse.setStatus(task.getStatus());
        return taskResponse;
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
    public Page<TaskResponse> getTasks(Integer page, Integer numTasks, String sort){
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, ALLOWED_SORT_FIELDS);
        return taskRepository.findAll(pageable).map(this::createTaskResponse);
    }

    @Override
    @Transactional
    public void deleteTaskById(Integer id) {
        taskRepository.delete(findTaskById(id));
    }

    @Override
    public TaskResponse addTask(TaskRequest taskRequest, Integer userId) {
        User user = userUtils.findUserById(userId);
        Task task = createTaskFromRequest(taskRequest, user);
        task = taskRepository.save(task);
        return createTaskResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(TaskRequest taskRequest,Integer id) {
        Task existingTask = findTaskById(id);
        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setStatus(taskRequest.getStatus());
        existingTask = taskRepository.save(existingTask);
        return createTaskResponse(existingTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByUserId(Integer page, Integer numTasks, String sort, Integer userId){
        userUtils.findUserById(userId);
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, ALLOWED_SORT_FIELDS);
        return taskRepository.findByUserId(userId, pageable).map(this::createTaskResponse);
    }

    @Override
    public Page<TaskResponse> getTasksByStatus(Integer page, Integer numTasks, String sort, Status status) {
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, ALLOWED_SORT_FIELDS);
        return taskRepository.findByStatus(status, pageable).map(this::createTaskResponse);
    }

    @Override
    public Page<TaskResponse> getTasksByUserIdAndStatus(Integer page, Integer numTasks, String sort, Integer userId, Status status) {
        userUtils.findUserById(userId);
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, ALLOWED_SORT_FIELDS);
        return taskRepository.findByUserIdAndStatus(userId, status, pageable).map(this::createTaskResponse);
    }

    @Override
    public Page<TaskResponse> getTasksByTitle(Integer page, Integer numTasks, String sort, String title) {
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, ALLOWED_SORT_FIELDS);
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable).map(this::createTaskResponse);
    }

    @Override
    public boolean isTaskOwner(Integer taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));
        return task.getUser().getUsername().equals(username);
    }

}
