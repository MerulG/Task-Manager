package com.project.taskmanager.service.impl;

import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;
import com.project.taskmanager.service.UserService;
import com.project.taskmanager.utils.PaginationUtils;
import com.project.taskmanager.utils.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "username", "email");


    public UserServiceImpl(UserRepository userRepository, UserUtils userUtils) {
        this.userRepository = userRepository;
        this.userUtils = userUtils;
    }

    private User createUserFromRequest(UserRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        return user;
    }

    private UserResponse createUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Integer id) {
        return createUserResponse(userUtils.findUserById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Integer page, Integer numTasks, String sort) {
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, ALLOWED_SORT_FIELDS);
        return userRepository.findAll(pageable).map(this::createUserResponse);
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = userRepository.save(createUserFromRequest(userRequest));
        return createUserResponse(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        User user = userUtils.findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Integer id) {
        User existingUser = userUtils.findUserById(id);

        if (!existingUser.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!existingUser.getUsername().equals(userRequest.getUsername())
                && userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPassword(userRequest.getPassword());
        existingUser = userRepository.save(existingUser);
        return createUserResponse(existingUser);
    }
}
