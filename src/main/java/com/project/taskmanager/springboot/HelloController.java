package com.project.taskmanager.springboot;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Yeeeeeeee3";
    }

}
