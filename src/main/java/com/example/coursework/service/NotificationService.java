package com.example.coursework.service;

import com.example.coursework.model.Notification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    // возвращает непрочитанные уведомления пользователя
    public List<Notification> getPending(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isReceived())
                .collect(Collectors.toList());
    }

    // добавляет новое уведомление в список
    public void addNotification(Notification notification) {
        notification.setId(UUID.randomUUID());
        notifications.add(notification);
    }

    // возвращает конкретное уведомление по его ID
    public Optional<Notification> getNotificationById(UUID id) {
        return notifications.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    // обновляет уведомление: заменяет старое на новое
    public void updateNotification(Notification notification) {
        // Проверка существования уведомления
        boolean exists = notifications.stream()
                .anyMatch(n -> n.getId().equals(notification.getId()));
        if (!exists) {
            throw new IllegalArgumentException("Уведомление с ID " + notification.getId() + " не найдено");
        }
        // Обновление
        notifications.removeIf(n -> n.getId().equals(notification.getId()));
        notifications.add(notification);
    }
}
