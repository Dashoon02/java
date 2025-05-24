package com.example.coursework.service;

import com.example.coursework.model.User;
import com.example.coursework.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetOrCreateUser_createsNewUserIfNotExists() {
        String username = "test_user";

        // Сначала пользователь не найден
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Сохраняем пользователя и возвращаем его же
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.getOrCreateUser(username);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertNotNull(user.getId());

        // Убедимся, что вызван метод save
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetOrCreateUser_returnsExistingUser() {
        String username = "existing_user";
        User existingUser = User.builder()
                .id(UUID.randomUUID().toString())
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        User result = userService.getOrCreateUser(username);

        assertEquals(existingUser, result);

        // Убедимся, что save НЕ вызывался
        verify(userRepository, never()).save(any());
    }
}
