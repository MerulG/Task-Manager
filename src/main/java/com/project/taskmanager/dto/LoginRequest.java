package com.project.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "The username of the user", example = "johndoe")
    @NotBlank(message = "Username is required")
    private String username;
    @Schema(description = "The password for the user account", example = "SecureP@ssw0rd")
    @NotBlank(message = "Password is required")
    private String password;
}
