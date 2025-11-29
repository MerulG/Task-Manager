package com.project.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @Schema(description = "The username of the user", example = "johndoe")
    @NotBlank(message = "Username is required")
    private String username;
    @Schema(description = "The email address of the user", example = "johndoe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;
    @Schema(description = "The password for the user account", example = "SecureP@ssw0rd")
    @NotBlank(message = "Password is required")
    private String password;
}
