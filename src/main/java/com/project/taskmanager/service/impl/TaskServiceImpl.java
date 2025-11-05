package com.project.taskmanager.service.impl;

import com.project.taskmanager.enums.Priority;
import com.project.taskmanager.enums.Status;
import com.project.taskmanager.model.Task;
import com.project.taskmanager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    List<Task> tasks = new ArrayList<>(List.of(
            new Task(1, "title1", "description1", Priority.MEDIUM, Status.NOT_STARTED),
            new Task(2, "title2", "description2", Priority.LOW, Status.NOT_STARTED),
            new Task(3, "title3", "description3", Priority.MEDIUM, Status.IN_PROGRESS),
            new Task(4, "title4", "description4", Priority.HIGH, Status.COMPLETED),
            new Task(5, "title5", "description5", Priority.VERY_HIGH, Status.NOT_STARTED)
    ));

    @Override
    public Task getTaskById(Integer id) {
        return null;
    }

    @Override
    public List<Task> getTasks() {
        return List.of();
    }

    @Override
    public void deleteTaskById(Integer id) {

    }

    @Override
    public Task addTask(Integer id, Task task) {
        return null;
    }

    @Override
    public Task updateTask(Integer id, Task task) {
        return null;
    }
}
