package com.example.coursework.controller;

import com.example.coursework.model.User;
import com.example.coursework.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    private User user;

    /**
     * Инициализация моков и тестируемого контроллера перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);

        user = new User();
        user.setId("1L");
        user.setUsername("testuser");
    }

    /**
     * Тест проверяет, что при вызове метода login с существующим именем пользователя:
     * возвращается HTTP-ответ со статусом 200 (OK);
     * в теле ответа содержится ожидаемый пользователь;
     * метод getOrCreateUser сервиса вызывается один раз.
     */
    @Test
    void login_ShouldReturnUser() {
        when(userService.getOrCreateUser("testuser")).thenReturn(user);

        ResponseEntity<User> response = userController.login("testuser");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getOrCreateUser("testuser");
    }
}
