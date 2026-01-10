package com.project.taskmanager.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider();

    @Test
    void shouldGenerateAndParseTokenCorrectly() {
        // Arrange
        User userDetails = new User("testuser", "password", Collections.emptyList());

        // Act
        String token = jwtProvider.generateToken(userDetails);
        String usernameFromToken = jwtProvider.getUsernameFromToken(token);

        // Assert
        assertNotNull(token);
        assertEquals("testuser", usernameFromToken);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        // Arrange
        User userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtProvider.generateToken(userDetails);

        // Act and Assert
        assertTrue(jwtProvider.validateToken(token, userDetails));
    }

    @Test
    void shouldInvalidateTokenForWrongUsername() {
        // Arrange
        User userDetails = new User("testuser", "password", Collections.emptyList());
        User otherUser = new User("otheruser", "password", Collections.emptyList());
        String token = jwtProvider.generateToken(userDetails);

        // Act and Assert
        assertFalse(jwtProvider.validateToken(token, otherUser));
    }
}