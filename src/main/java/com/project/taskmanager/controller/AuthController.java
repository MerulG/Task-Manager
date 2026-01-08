package com.project.taskmanager.controller;

import com.project.taskmanager.dto.*;
import com.project.taskmanager.security.JwtProvider;
import com.project.taskmanager.service.UserService;
import com.project.taskmanager.service.impl.ApiUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
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

    @Operation(
            summary = "Login to account",
            description = "Login with an existing username and password"
    )
    @PostMapping("/login")
    public LoginResponse login(
            @Parameter(description = "Login credentials containing username and password") @RequestBody @Valid LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtProvider.generateToken(userDetails);
            return new LoginResponse(token);
        }  catch (org.springframework.security.core.AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

    }
}
