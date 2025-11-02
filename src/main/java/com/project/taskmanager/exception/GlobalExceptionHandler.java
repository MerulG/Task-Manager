package com.project.taskmanager.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    //MethodArgumentNotValidException for the @valid annotation
    //ResponseStatusException for find task by id exception
}