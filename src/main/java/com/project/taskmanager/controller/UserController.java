package com.project.taskmanager.controller;

import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.service.UserService;
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

    @GetMapping("id/{id}")
    public UserResponse getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping()
    public Page<UserResponse> getUsers(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer numTasks,
                                       @RequestParam(defaultValue = "id,asc") String sort){
        return userService.getUsers(page,numTasks,sort);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deteteUser(@PathVariable Integer id){
        userService.deleteUserById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest){
        return userService.addUser(userRequest);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Integer id, @RequestBody @Valid UserRequest userRequest){
        return userService.updateUser(userRequest, id);
    }

}
