package com.project.taskmanager.utils;

import com.project.taskmanager.exception.UserNotFoundException;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUtils userUtils;

    @Test
    void shouldFindUserByIdWhenUserExists() {
        //arrange
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        //act
        User foundUser = userUtils.findUserById(1);
        //assert
        assertEquals(1,foundUser.getId(),"User ID should match the requested ID");
        verify(userRepository,times(1)).findById(1);

    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExists() {
        //arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        //act
        //assert
        assertThrows(UserNotFoundException.class, () -> userUtils.findUserById(999));
        verify(userRepository,times(1)).findById(999);

    }
}
