package com.example.coursework.controller;

import com.example.coursework.model.Notification;
import com.example.coursework.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    private NotificationController notificationController;

    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationService);

        notification1 = new Notification();
        notification1.setId(UUID.randomUUID());
        notification1.setUserId("user1");
        notification1.setMessage("Notification 1");

        notification2 = new Notification();
        notification2.setId(UUID.randomUUID());
        notification2.setUserId("user1");
        notification2.setMessage("Notification 2");
    }

    /**
     * Проверяем, что метод getAll возвращает все уведомления пользователя
     */
    @Test
    void getAll_ShouldReturnAllNotifications() {
        List<Notification> expected = List.of(notification1, notification2);
        when(notificationService.getAll("user1")).thenReturn(expected);

        List<Notification> actual = notificationController.getAll("user1");

        assertEquals(expected, actual);
        verify(notificationService, times(1)).getAll("user1");
    }

    /**
     * Проверяем, что добавленное уведомление возвращается обратно
     */
    @Test
    void addNotification_ShouldReturnNotification() {
        when(notificationService.addNotification(notification1)).thenReturn(notification1);

        Notification actual = notificationController.addNotification(notification1);

        assertEquals(notification1, actual);
        verify(notificationService, times(1)).addNotification(notification1);
    }

    /**
     * Проверяем, что метод getById возвращает список уведомлений по ID
     */
    @Test
    void getById_ShouldReturnNotificationsList() {
        UUID id = notification1.getId();
        List<Notification> expected = List.of(notification1);
        when(notificationService.getNotificationById(id, false)).thenReturn(expected);

        List<Notification> actual = notificationController.getById(id, false);

        assertEquals(expected, actual);
        verify(notificationService, times(1)).getNotificationById(id, false);
    }

    /**
     * Проверяем, что если уведомление по ID не найдено — выбрасывается ResponseStatusException с кодом 404
     */
    @Test
    void getById_ShouldThrowExceptionIfEmpty() {
        UUID id = UUID.randomUUID();
        when(notificationService.getNotificationById(id, true)).thenReturn(List.of());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            notificationController.getById(id, true);
        });

        assertEquals("Уведомление не найдено", ex.getReason());
        assertEquals(404, ex.getStatusCode().value());
        verify(notificationService, times(1)).getNotificationById(id, true);
    }
}
