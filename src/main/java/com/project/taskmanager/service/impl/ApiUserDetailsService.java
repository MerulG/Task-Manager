package com.project.taskmanager.service.impl;

import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;
import com.project.taskmanager.security.ApiUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApiUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }

        User user = optionalUser.get();

        // Return custom UserDetails with ID
        return new ApiUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
}
