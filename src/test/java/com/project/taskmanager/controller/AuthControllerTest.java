package com.project.taskmanager.controller;

import com.project.taskmanager.dto.RegisterRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.security.JwtAuthenticationFilter;
import com.project.taskmanager.security.JwtProvider;
import com.project.taskmanager.service.UserService;
import com.project.taskmanager.service.impl.ApiUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ApiUserDetailsService apiUserDetailsService;

    @Test
    void shouldRegisterWhenJsonIsValid() throws Exception {
        //arrange
        String validJson = """
        {
            "username": "newuser",
            "email": "newuser@example.com",
            "password": "Password1!"
        }
        """;
        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setId(1);
        expectedResponse.setUsername("newuser");
        expectedResponse.setEmail("newuser@example.com");
        when(userService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));

        verify(userService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenRegsiterUserWithInvalidJson() throws Exception {
        //arrange
        String inValidJson = """
        {
            "username": "newuser",
            "email": "",
            "password": "Password1!"
        }
        """;
        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setId(1);
        expectedResponse.setUsername("newuser");
        expectedResponse.setEmail("newuser@example.com");
        when(userService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inValidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("email: Email is required"));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }
    
    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String invalidJson = """
        {
            "email": "test@example.com",
            "password": "Password1!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenUsernameIsBlank() throws Exception {
        String invalidJson = """
        {
            "username": "  ",
            "email": "test@example.com",
            "password": "Password1!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenUsernameIsMissing() throws Exception {
        String invalidJson = """
        {
            "email": "test@example.com",
            "password": "Password1!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "email": "test@example.com",
            "password": "Pass1!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordMissingUppercase() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "email": "test@example.com",
            "password": "password1!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordMissingLowercase() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "email": "test@example.com",
            "password": "PASSWORD1!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordMissingNumber() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "email": "test@example.com",
            "password": "Password!"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordMissingSpecialCharacter() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "email": "test@example.com",
            "password": "Password1"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissing() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "email": "test@example.com"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(userService, times(0)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        String validJson = """
        {
            "username": "newuser",
            "email": "existing@example.com",
            "password": "Password1!"
        }
        """;

        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenUsernameAlreadyExists() throws Exception {
        String validJson = """
        {
            "username": "existinguser",
            "email": "new@example.com",
            "password": "Password1!"
        }
        """;

        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        String loginJson = """
        {
            "username": "testuser",
            "password": "Password1!"
        }
        """;

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);

        when(authenticationManager.authenticate(any()))
                .thenReturn(mockAuth);

        when(jwtProvider.generateToken(any(UserDetails.class)))
                .thenReturn("fake-jwt-token");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtProvider, times(1)).generateToken(any());
    }

    @Test
    void shouldFailLoginWithInvalidPasswordCredentials() throws Exception {
        // Arrange
        String loginJson = """
        {
            "username": "testuser",
            "password": "WrongPassword1!"
        }
        """;

        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Bad credentials") {});

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.messages[0]").value("Invalid username or password"));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtProvider, times(0)).generateToken(any());
    }

    @Test
    void shouldReturnUnauthorizedWhenUsernameIsIncorrect() throws Exception {
        String invalidJson = """
        {
            "username": "wronguser",
            "password": "Password1!"
        }
        """;

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, times(0)).generateToken(any(UserDetails.class));
    }

    @Test
    void shouldReturnBadRequestWhenLoginUsernameIsMissing() throws Exception {
        String invalidJson = """
        {
            "password": "Password1!"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldReturnBadRequestWhenLoginPasswordIsMissing() throws Exception {
        String invalidJson = """
        {
            "username": "testuser"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldReturnBadRequestWhenLoginUsernameIsBlank() throws Exception {
        String invalidJson = """
        {
            "username": "  ",
            "password": "Password1!"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldReturnBadRequestWhenLoginPasswordIsBlank() throws Exception {
        String invalidJson = """
        {
            "username": "testuser",
            "password": "  "
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldReturnBadRequestWhenLoginRequestIsEmpty() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


}
