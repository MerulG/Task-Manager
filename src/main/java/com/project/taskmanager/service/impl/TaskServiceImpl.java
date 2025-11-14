package com.project.taskmanager.service.impl;

import com.project.taskmanager.dto.TaskRequest;
import com.project.taskmanager.dto.TaskResponse;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.exception.TaskNotFoundException;
import com.project.taskmanager.exception.UserNotFoundException;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.TaskRepository;
import com.project.taskmanager.repository.UserRepository;
import com.project.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus()
        );
    }

    private List<TaskResponse> createTaskResponseList(List<Task> tasks){
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks) {
            taskResponses.add(createTaskResponse(task));
        }
        return taskResponses;
    }

    private Pageable validateAndCreatePageable(Integer page, Integer numTasks, String sort){
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative.");
        }
        if (page > 100){
            throw new IllegalArgumentException("Page number cannot be greater than 100.");
        }
        if (numTasks <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
        if (numTasks > 100) {
            throw new IllegalArgumentException("Page size cannot be greater than 100.");
        }
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("Sort must be specified.");
        }

        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];

        List<String> allowedSortFields = List.of("id", "title","description", "status", "priority");
        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort.Direction direction = Sort.Direction.ASC;  // default
        if (sortParts.length > 1) {
            String sortOrder = sortParts[1];
            if (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sort order: must be 'asc' or 'desc'.");
            }else if(sortOrder.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }
        }
        return PageRequest.of(page,numTasks,Sort.by(direction,sortBy));
    }

    public Task findTaskById(Integer id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isEmpty()) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        return task.get();
    }

    public User findUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user.get();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTask(Integer id) {
        return createTaskResponse(findTaskById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(Integer page, Integer numTasks, String sort){
        Pageable pageable = validateAndCreatePageable(page, numTasks, sort);
        return taskRepository.findAll(pageable).map(this::createTaskResponse);
    }

    @Override
    @Transactional
    public void deleteTaskById(Integer id) {
        taskRepository.delete(findTaskById(id));
    }

    @Override
    public TaskResponse addTask(TaskRequest taskRequest, Integer userId) {
        User user = findUserById(userId);
        Task task = createTaskFromRequest(taskRequest, user);
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

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasksByUserId(Integer page, Integer numTasks, String sort, Integer userId){
        findUserById(userId);
        Pageable pageable = validateAndCreatePageable(page, numTasks, sort);
        return taskRepository.findByUserId(userId, pageable).map(this::createTaskResponse);
    }

    @Override
    public Page<TaskResponse> getTasksByStatus(Integer page, Integer numTasks, String sort, Status status) {
        Pageable pageable = validateAndCreatePageable(page, numTasks, sort);
        return taskRepository.findByStatus(status, pageable).map(this::createTaskResponse);
    }

    @Override
    public Page<TaskResponse> getTasksByUserIdAndStatus(Integer page, Integer numTasks, String sort, Integer userId, Status status) {
        findUserById(userId);
        Pageable pageable = validateAndCreatePageable(page, numTasks, sort);
        return taskRepository.findByUserIdAndStatus(userId, status, pageable).map(this::createTaskResponse);
    }

    @Override
    public Page<TaskResponse> getTasksByTitle(Integer page, Integer numTasks, String sort, String title) {
        Pageable pageable = validateAndCreatePageable(page, numTasks, sort);
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable).map(this::createTaskResponse);
    }

}
