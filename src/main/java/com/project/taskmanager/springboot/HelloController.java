package com.project.taskmanager.springboot;

import com.project.taskmanager.Task;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        Task task = new Task(1, "test", "test", "test");
        return task.toString();
    }

}
