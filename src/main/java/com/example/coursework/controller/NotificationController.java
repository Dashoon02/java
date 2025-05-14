package com.example.coursework.controller;

import com.example.coursework.model.Notification;
import com.example.coursework.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Notification addNotification(@RequestBody Notification notification) {
        notificationService.addNotification(notification);
        return notification;
    }

    @PatchMapping("/{id}/mark-as-read")
    public void markAsRead(@PathVariable UUID id, @RequestBody Map<String, Boolean> request) {
        boolean received = request.get("received");
        Optional<Notification> notification = notificationService.getNotificationById(id);
        notification.ifPresent(n -> {
            n.setReceived(received);
            notificationService.updateNotification(n);
        });
    }
}

