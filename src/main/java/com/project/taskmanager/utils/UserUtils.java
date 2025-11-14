package com.project.taskmanager.utils;

import com.project.taskmanager.exception.UserNotFoundException;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;

import java.util.Optional;

public final class UserUtils {

    public static User findUserById(UserRepository userRepository, Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user.get();
    }
}
