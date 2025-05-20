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

    @GetMapping("/pending")
    public List<Notification> getPending(@RequestParam String userId) {
        return notificationService.getPending(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification addNotification(@RequestBody Notification notification) {
        notificationService.addNotification(notification);
        return notification;
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable UUID id) {
        return notificationService.getNotificationById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }

    @PatchMapping("/{id}/mark-as-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable UUID id) {
        Notification notification = notificationService.getNotificationById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));

        notification.setReceived(true);
        notificationService.updateNotification(notification);
    }
}

