package com.project.taskmanager.controller;

import com.project.taskmanager.dto.RegisterRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with hashed password"
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(
            @Parameter(description = "User data containing username and email") @RequestBody @Valid RegisterRequest registerRequest){
        return userService.register(registerRequest);
    }
}
