package com.project.taskmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    // Mocked UserDetails
    private UserDetails userDetails;

    // Example secret for testing
    private final String testSecret = "testsecret123456789012345678901234567890";
    private final long testExpirationMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        // manually inject the properties
        jwtProvider.setJwtSecret(testSecret);
        jwtProvider.setExpirationMs(testExpirationMs);
        jwtProvider.init(); // initialize the key

        // mock user details
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        // Act
        String token = jwtProvider.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        // Arrange
        String token = jwtProvider.generateToken(userDetails);

        // Act
        String username = jwtProvider.getUsernameFromToken(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        // Arrange
        String token = jwtProvider.generateToken(userDetails);

        // Act
        boolean isValid = jwtProvider.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidUsername() {
        // Arrange
        String token = jwtProvider.generateToken(userDetails);
        UserDetails anotherUser = mock(UserDetails.class);
        when(anotherUser.getUsername()).thenReturn("wronguser");

        // Act
        boolean isValid = jwtProvider.validateToken(token, anotherUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_shouldReturnFalseForMalformedToken() {
        // Arrange
        String badToken = "this.is.not.a.jwt";

        // Act
        boolean isValid = jwtProvider.validateToken(badToken, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        // Use a very short expiration to simulate expiry
        jwtProvider.setExpirationMs(10); // 10ms
        jwtProvider.init();
        String token = jwtProvider.generateToken(userDetails);

        // Wait for token to expire
        Thread.sleep(20);

        // Act
        boolean isValid = jwtProvider.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid);
    }
}