package com.project.taskmanager.controller;

import com.project.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(TaskController.class) // Load only TaskController & MVC context
class TaskControllerTest {

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1);
        sampleTask.setTitle("Sample Task");
        sampleTask.setDescription("Test task description");
    }

    @Test
    void shouldReturnTaskById() {
    }
}
