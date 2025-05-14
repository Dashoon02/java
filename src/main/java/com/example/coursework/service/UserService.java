package com.example.coursework.service;

import com.example.coursework.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    /**
     * используем как временную базу данных для хранения зарегестрированных пользователей
     */

    // ключ - имя пользователя, значение - объект класса User
    private final Map<String, User> users = new HashMap<>();

    public User getOrCreateUser(String username) {
        return users.computeIfAbsent(username, name ->
                User.builder()
                        .id(UUID.randomUUID().toString())
                        .username(name)
                        .build()
        );
    }

}
