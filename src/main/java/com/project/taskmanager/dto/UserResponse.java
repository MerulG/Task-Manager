package com.project.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponse {
    @Schema(description = "The unique identifier of the user", example = "1")
    private Integer id;
    @Schema(description = "The username of the user", example = "johndoe")
    private String username;
    @Schema(description = "The email address of the user", example = "johndoe@example.com")
    private String email;
}
