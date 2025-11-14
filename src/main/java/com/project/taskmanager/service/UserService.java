package com.project.taskmanager.service;

import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse getUser(Integer id);
    Page<UserResponse> getUsers(Integer page, Integer numTasks, String sort);
    UserResponse addUser(UserRequest userRequest);
    void deleteUserById(Integer id);
    UserResponse updateUser(UserRequest userRequest, Integer id);

}
