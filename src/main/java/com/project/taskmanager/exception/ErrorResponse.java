package com.project.taskmanager.exception;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class ErrorResponse {
    private final Object time;
    private int status;
    private List<String> messages;
    private String path;

    public ErrorResponse(int status, List<String> messages, String path) {
        this.time = Instant.now();
        this.status = status;
        this.messages = messages;
        this.path = path;
    }
}
