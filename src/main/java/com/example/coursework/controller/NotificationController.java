package com.example.coursework.controller;

import com.example.coursework.model.Notification;
import com.example.coursework.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAll(@RequestParam String userId) {
        return notificationService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification addNotification(@RequestBody Notification notification) {
        notificationService.addNotification(notification);
        return notification;
    }

    @GetMapping("/{id}")
    public List<Notification> getById(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "false") boolean notificationsBefore) {

        List<Notification> result = notificationService.getNotificationById(id, notificationsBefore);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Уведомление не найдено");
        }

        return result;
    }
}
