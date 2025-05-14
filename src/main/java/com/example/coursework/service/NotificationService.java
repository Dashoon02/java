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

    public List<Notification> getAll(String userId) {
        /**
         * возвращает все уведомления для пользователя
         */
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Notification> getPending(String userId) {
        /**
         * возвращает непрочитанные уведомления пользователя
         */
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isReceived())
                .collect(Collectors.toList());
    }

    public void addNotification(Notification notification) {
        /**
         * добавляет новое уведомление в список
         */
        notification.setId(UUID.randomUUID());
        notifications.add(notification);
    }

    public Optional<Notification> getNotificationById(UUID id) {
        return notifications.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    public void updateNotification(Notification notification) {
        notifications.removeIf(n -> n.getId().equals(notification.getId()));
        notifications.add(notification);
    }
}
