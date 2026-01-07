package com.project.taskmanager.service;

import com.project.taskmanager.dto.UserRequest;
import com.project.taskmanager.dto.UserResponse;
import com.project.taskmanager.model.User;
import com.project.taskmanager.repository.UserRepository;
import com.project.taskmanager.service.impl.UserServiceImpl;
import com.project.taskmanager.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserUtils userUtils;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnUserByIdWhenExists(){
        //arrange
        User user = new User();
        user.setId(1);
        when(userUtils.findUserById(1)).thenReturn(user);
        //act
        UserResponse response = userService.getUser(1);
        //assert
        assertEquals(1,response.getId(),"User ID should match the requested ID");
        verify(userUtils,times(1)).findUserById(1);
    }

    @Test
    void shouldReturnAllUsers(){
        //arrange
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        Page<User> pageOfUsers = new PageImpl<>(List.of(user1, user2));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(pageOfUsers);
        //act
        Page<UserResponse> users = userService.getUsers(1, 5, "id");
        //assert
        assertEquals(2,users.getTotalElements());
        assertEquals(2,users.getNumberOfElements());
        assertEquals(1,users.getContent().get(0).getId());
        assertEquals(2,users.getContent().get(1).getId());
        verify(userRepository,times(1)).findAll(any(Pageable.class));

    }

    @Test
    void shouldCreateNewUser(){
        //arrange
        UserRequest request= new UserRequest();
        request.setUsername("test");
        request.setEmail("test@email.com");
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        //act
        UserResponse response = userService.register(request);
        //assert
        assertEquals(1, response.getId(),"User ID should match the requested ID");
        assertEquals("test", response.getUsername(),"User username should match the expected username");
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingUserAndEmailExists(){
        //arrange
        UserRequest request= new UserRequest();
        request.setEmail("test@email.com");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        //act
        //assert
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        verify(userRepository,times(1)).existsByEmail(request.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenAddingUserAndUsernameExists(){
        //arrange
        UserRequest request= new UserRequest();
        request.setUsername("test");
        request.setEmail("test@email.com");
        when(userRepository.existsByEmail(anyString())).thenReturn(false); //service checks email first
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        //act
        //assert
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        verify(userRepository,times(1)).existsByUsername(request.getUsername());
    }

    @Test
    void shouldDeleteUser(){
        //arrange
        User user = new User();
        user.setId(1);
        when(userUtils.findUserById(1)).thenReturn(user);
        //act
        userService.deleteUserById(1);
        //assert
        verify(userUtils,times(1)).findUserById(1);
        verify(userRepository,times(1)).delete(any(User.class));
    }

    @Test
    void shouldUpdateUser(){
        //arrange
        UserRequest request = new UserRequest();
        request.setEmail("email2@email");
        request.setUsername("username2");
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setEmail("email1@email");
        existingUser.setUsername("username1");
        when(userUtils.findUserById(1)).thenReturn(existingUser);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        //get the first argument passed to save method
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //act
        UserResponse response = userService.updateUser(request,1);
        //assert
        assertEquals(1, response.getId(),"User ID should match the requested ID");
        assertEquals("username2", response.getUsername(),"User username should match the expected username");
        assertEquals("email2@email", response.getEmail(),"User email should match the expected email");
        verify(userRepository,times(1)).save(any(User.class));
        verify(userRepository,times(1)).existsByEmail(request.getEmail());
        verify(userRepository,times(1)).existsByUsername(request.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserAndEmailExists(){
        //arrange
        UserRequest request = new UserRequest();
        request.setEmail("email@email");
        request.setUsername("username");
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setEmail("email2@email");
        existingUser.setUsername("username2");
        when(userUtils.findUserById(1)).thenReturn(existingUser);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        //act
        //assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(request,1));
        verify(userRepository,times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserAndUsernameExists(){
        //arrange
        UserRequest request = new UserRequest();
        request.setEmail("email@email");
        request.setUsername("username");
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setEmail("email2@email");
        existingUser.setUsername("username2");
        when(userUtils.findUserById(1)).thenReturn(existingUser);
        when(userRepository.existsByEmail(anyString())).thenReturn(false); //service checks email first
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);
        //act
        //assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(request,1));
        verify(userRepository,times(1)).existsByEmail(request.getEmail());
        verify(userRepository,times(1)).existsByUsername(request.getUsername());
        verify(userRepository, never()).save(any());
    }

}
