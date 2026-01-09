package com.project.taskmanager.service.impl;

import com.project.taskmanager.dto.RegisterRequest;
import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.enums.Role;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;
import com.project.taskmanager.service.UserService;
import com.project.taskmanager.utils.PaginationUtils;
import com.project.taskmanager.utils.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "username", "email");
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, UserUtils userUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userUtils = userUtils;
        this.passwordEncoder = passwordEncoder;
    }

    private User registerUserFromRequest(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.USER);
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
    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = userRepository.save(registerUserFromRequest(registerRequest));
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

        if (userRequest.getEmail() != null && !userRequest.getEmail().isBlank()) {
            if (!existingUser.getEmail().equals(userRequest.getEmail())
                    && userRepository.existsByEmail(userRequest.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            existingUser.setEmail(userRequest.getEmail());
        }

        if (userRequest.getUsername() != null && !userRequest.getUsername().isBlank()) {
            if (!existingUser.getUsername().equals(userRequest.getUsername())
                    && userRepository.existsByUsername(userRequest.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            existingUser.setUsername(userRequest.getUsername());
        }

        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            existingUser.setPassword(hashedPassword);
        }

        if (userRequest.getRole() != null) {
            existingUser.setRole(userRequest.getRole());
        }

        existingUser = userRepository.save(existingUser);
        return createUserResponse(existingUser);
    }

    @Override
    public boolean isUser(Integer userId, String username) {
        User user = userUtils.findUserById(userId);
        return user.getUsername().equals(username);
    }
}
