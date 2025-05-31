package com.example.coursework.service;

import com.example.coursework.model.Notification;
import org.springframework.stereotype.Service;
import com.example.coursework.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    // возвращает все уведомления для пользователя
    public List<Notification> getAll(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    // добавляет новое уведомление в список
    public Notification addNotification(Notification notification) {
        notification.setId(null); // чтобы гарантировать, что id будет сгенерирован
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    /**
     * notificationsBefore = false: возвращает конкретное уведомление по его ID
     * notificationsBefore = true: возвращает все уведомления, которые были до указанного (включительно)
     */
    public List<Notification> getNotificationById(UUID id, boolean notificationsBefore) {
        Notification target = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Уведомление с ID " + id + " не найдено"));

        if (!notificationsBefore) {
            return List.of(target);
        }

        LocalDateTime targetDate = target.getCreatedAt();
        // Берём все уведомления пользователя до targetDate включительно
        return notificationRepository.findByUserId(target.getUserId()).stream()
                .filter(n -> !n.getCreatedAt().isAfter(targetDate))
                .sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()))
                .toList();
    }

}
