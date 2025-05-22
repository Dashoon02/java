package com.example.coursework.service;

import com.example.coursework.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Инициализация нового экземпляра UserService перед каждым тестом
        userService = new UserService();
    }

    /**
     * Проверяем, что если пользователь с таким именем не существует, он будет создан
     */
    @Test
    void testGetOrCreateUser_createsNewUserIfNotExists() {

        String username = "test_user";
        User user = userService.getOrCreateUser(username);

        assertNotNull(user); // Убедиться, что пользователь не null
        assertEquals(username, user.getUsername()); // Имя пользователя должно совпадать
        assertNotNull(user.getId()); // У пользователя должен быть присвоен ID
    }

    /**
     * Проверяем, что если пользователь уже существует, метод возвращает того же самого пользователя
     */
    @Test
    void testGetOrCreateUser_returnsExistingUser() {

        String username = "existing_user";
        User first = userService.getOrCreateUser(username);  // создаём нового пользователя
        User second = userService.getOrCreateUser(username); // получаем его же повторно

        assertSame(first, second); // Убедиться, что возвращается тот же объект (а не новый)
    }
}
