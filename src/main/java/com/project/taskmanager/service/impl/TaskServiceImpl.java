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
    public List<TaskResponse> getAllTasks() {
        List<Task> allTasks = taskRepository.findAll();
        return createTaskResponseList(allTasks);
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
    public List<TaskResponse> getTasksByUserId(Integer userId){
        List<Task> tasks = taskRepository.findByUserId(userId);
        return createTaskResponseList(tasks);
    }

    @Override
    public List<TaskResponse> getTasksByStatus(Status status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return createTaskResponseList(tasks);
    }

    @Override
    public List<TaskResponse> getTasksByUserIdAndStatus(Integer userId, Status status) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(userId, status);
        return createTaskResponseList(tasks);
    }

    @Override
    public List<TaskResponse> getTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return createTaskResponseList(tasks);
    }

}
