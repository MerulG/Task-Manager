package com.project.taskmanager.security;

import com.project.taskmanager.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ApiUserDetailsTest {

    @Test
    void testApiUserDetailsGettersAndAuthorities() {
        // Arrange
        Integer id = 1;
        String username = "testuser";
        String password = "Password1!";
        Role role = Role.ADMIN;

        ApiUserDetails userDetails = new ApiUserDetails(id, username, password, role);

        // Act & Assert
        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}