package com.example.coursework.service;

import com.example.coursework.model.User;
import com.example.coursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .id(UUID.randomUUID().toString())
                            .username(username)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
