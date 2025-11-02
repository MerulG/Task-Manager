package com.project.taskmanager.controller;

import com.project.taskmanager.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    List<Task> tasks = new ArrayList<>(List.of(
            new Task(1, "title1", "description1", "priority1", "status1"),
            new Task(2, "title2", "description2", "priority2", "status2"),
            new Task(3, "title3", "description3", "priority3", "status3"),
            new Task(4, "title4", "description4", "priority4", "status4"),
            new Task(5, "title5", "description5", "priority5", "status5")
    ));

    @GetMapping
    public List<Task> getTasks() {
        return tasks;
    }

    @GetMapping("{id}")
    public Task getTaskById(@PathVariable Integer id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Integer id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTask(@RequestBody Task task) {
        tasks.add(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Integer id, @RequestBody Task updatedTask) {
        Task existingTask = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setStatus(updatedTask.getStatus());
        return existingTask;
    }


}
