package com.example.coursework.controller;

import com.example.coursework.model.User;
import com.example.coursework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username) {
        User user = userService.getOrCreateUser(username);
        return ResponseEntity.ok(user);
    }
}
