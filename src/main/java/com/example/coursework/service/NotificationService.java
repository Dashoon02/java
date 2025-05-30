package com.example.coursework.service;

import com.example.coursework.model.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final List<Notification> notifications = new ArrayList<>();

    // возвращает все уведомления для пользователя
    public List<Notification> getAll(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // добавляет новое уведомление в список
    public void addNotification(Notification notification) {
        notification.setId(UUID.randomUUID());
        notification.setCreatedAt(LocalDateTime.now());
        notifications.add(notification);
    }

    /**
     * notificationsBefore = false: возвращает конкретное уведомление по его ID
     * notificationsBefore = true: возвращает все уведомления, которые были до указанного (включительно)
     */
    public List<Notification> getNotificationById(UUID id, boolean notificationsBefore) {
        Objects.requireNonNull(notifications, "Список уведомлений не может быть null");

        Notification target = notifications.stream()
                .filter(n -> id.equals(n.getId()))  // поменяли местами для null-safety
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Уведомление с ID " + id + " не найдено"));

        if (!notificationsBefore) {
            return List.of(target);
        }

        LocalDateTime targetDate = target.getCreatedAt();
        return notifications.stream()
                .filter(n -> !n.getCreatedAt().isAfter(targetDate))
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .toList(); // Java 16+ вместо collect(Collectors.toList())
    }

}
