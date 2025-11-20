package com.project.taskmanager.utils;

import com.project.taskmanager.exception.UserNotFoundException;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtils {

    private final UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user.get();
    }
}
