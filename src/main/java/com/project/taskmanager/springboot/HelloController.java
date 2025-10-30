package com.project.taskmanager.springboot;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        Blank blank = new Blank();
        blank.setBlankString("LOMBOKTEST2");
        System.out.println(blank.getBlankString());
        return "DEVTOOLSTEST5";
    }

}
