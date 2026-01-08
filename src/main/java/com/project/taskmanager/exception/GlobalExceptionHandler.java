package com.project.taskmanager.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException exception, HttpServletRequest request){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(exception.getMessage()),
                request.getRequestURI()
        );
        log.info("Task not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception, HttpServletRequest request){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                List.of(exception.getMessage()),
                request.getRequestURI()
        );
        log.info("User not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            StringBuilder message = new StringBuilder();
            message.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage());
            errors.add(message.toString());
        }
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                request.getRequestURI()
        );
        log.info("Validation failed: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumBody(HttpMessageNotReadableException exception, HttpServletRequest request){
        String message = "Invalid request body";
        if(exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetClass = invalidFormatException.getTargetType();
            if(targetClass.isEnum()) {
                //gets possible values for the enum causing problems
                Object[] enumConstants = targetClass.getEnumConstants();
                StringBuilder possibleValues = new StringBuilder();
                for (int i = 0; i < enumConstants.length; i++) {
                    possibleValues.append(enumConstants[i].toString());
                    if (i < enumConstants.length - 1) {
                        possibleValues.append(", ");
                    }
                }
                message = String.format("Invalid "+targetClass.getSimpleName()+" value. Possible values: %s", possibleValues);
            }
        }
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                List.of(message),
                request.getRequestURI()
        );
        log.info("Invalid request body: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumParam(MethodArgumentTypeMismatchException exception, HttpServletRequest request){
        String message = "Invalid value for request parameter";
        Class<?> targetClass = exception.getRequiredType();
        if(targetClass.isEnum()) {
            //gets possible values for the enum causing problems
            Object[] enumConstants = targetClass.getEnumConstants();
            StringBuilder possibleValues = new StringBuilder();
            for (int i = 0; i < enumConstants.length; i++) {
                possibleValues.append(enumConstants[i].toString());
                if (i < enumConstants.length - 1) {
                    possibleValues.append(", ");
                }
            }
            message = String.format("Invalid"+targetClass.getSimpleName()+" value. Possible values: %s", possibleValues);
        }
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                List.of(message),
                request.getRequestURI()
        );
        log.info("Invalid request parameter: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest request){
        String message = exception.getMessage() != null ? exception.getMessage() : "Invalid argument provided.";
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                List.of(message),
                request.getRequestURI()
        );
        log.info("Invalid argument: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatusCode().value(),
                List.of(ex.getReason() != null ? ex.getReason() : "Error"),
                request.getRequestURI()
        );
        log.info("ResponseStatusException: {}", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception, HttpServletRequest request){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of("Unexpected error occurred."),
                request.getRequestURI()
        );
        log.info("Unexpected error occurred:" + exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}