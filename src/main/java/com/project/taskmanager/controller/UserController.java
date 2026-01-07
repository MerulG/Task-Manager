package com.project.taskmanager.controller;

import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get a user by ID",
            description = "Retrieves a single user by their unique identifier."
    )
    @GetMapping("/{id}")
    public UserResponse getUser(
            @Parameter(description = "The unique identifier of the user") @PathVariable Integer id) {
        return userService.getUser(id);
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users with sorting options."
    )
    @GetMapping()
    public Page<UserResponse> getUsers(
            @Parameter(description = "Page number (zero-indexed)") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Number of users per page") @RequestParam(defaultValue = "10") Integer numTasks,
            @Parameter(description = "Sort criteria in format: property,(asc|desc)") @RequestParam(defaultValue = "id,asc") String sort){
        return userService.getUsers(page,numTasks,sort);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by their unique identifier."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "The unique identifier of the user to delete") @PathVariable Integer id){
        userService.deleteUserById(id);
    }

    @Operation(
            summary = "Update a user",
            description = "Updates an existing user with new information."
    )
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @Parameter(description = "The unique identifier of the user to update") @PathVariable Integer id,
            @Parameter(description = "Updated user data") @RequestBody @Valid UserRequest userRequest){
        return userService.updateUser(userRequest, id);
    }

}
