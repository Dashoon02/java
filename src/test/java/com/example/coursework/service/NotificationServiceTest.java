package com.example.coursework.service;

import com.example.coursework.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        //  // Инициализация нового экземпляра NotificationService перед каждым тестом
        notificationService = new NotificationService();
    }

    /**
     * Проверяеь, что при добавлении уведомления ему присваиваются ID и дата создания,
     * а также что оно сохраняется и может быть получено по userId
     */
    @Test
    void testAddNotification_assignsIdAndCreatedAt() {
        Notification n = Notification.builder()
                .userId("user1")
                .message("Test message")
                .build();

        notificationService.addNotification(n);

        assertNotNull(n.getId()); // Убедиться, что ID присвоен
        assertNotNull(n.getCreatedAt()); // Убедиться, что дата создания присвоена
        assertEquals(1, notificationService.getAll("user1").size()); // Проверка, что уведомление сохранено
    }

    /**
     * Проверяем, что метод getAll возвращает только уведомления,
     * относящиеся к указанному пользователю
     */
    @Test
    void testGetAll_returnsOnlyUserNotifications() {
        Notification n1 = Notification.builder().userId("user1").message("Msg 1").build();
        Notification n2 = Notification.builder().userId("user2").message("Msg 2").build();

        notificationService.addNotification(n1);
        notificationService.addNotification(n2);

        List<Notification> user1Notifications = notificationService.getAll("user1");

        assertEquals(1, user1Notifications.size()); // Убедиться, что вернулось одно уведомление
        assertEquals("user1", user1Notifications.get(0).getUserId()); // И что оно принадлежит user1
    }

    /**
     * Проверяем, что метод getNotificationById возвращает уведомление по ID корректно
     */
    @Test
    void testGetNotificationById_returnsCorrectNotification() {
        Notification n = Notification.builder().userId("user").message("Hello").build();
        notificationService.addNotification(n);

        List<Notification> result = notificationService.getNotificationById(n.getId(), false);

        assertEquals(1, result.size());
        assertEquals(n.getId(), result.get(0).getId()); // Убедиться, что ID совпадает
    }

    /**
     * Проверяем, что метод getNotificationById с флагом `before = true` возвращает уведомления,
     * созданные до указанного, включая его самого, в порядке от новых к старым
     */
    @Test
    void testGetNotificationById_withBeforeFlag() {
        Notification n1 = Notification.builder().userId("user").message("First").build();
        Notification n2 = Notification.builder().userId("user").message("Second").build();
        notificationService.addNotification(n1);
        try { Thread.sleep(10); } catch (InterruptedException ignored) {} // Обеспечиваем разницу по времени
        notificationService.addNotification(n2);

        List<Notification> result = notificationService.getNotificationById(n2.getId(), true);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt())); // Проверка сортировки
    }

    /**
     * Проверяем, что метод getNotificationById возвращает уведомление по ID корректно
     */
    @Test
    void testGetNotificationById_throwsIfNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(NoSuchElementException.class, () ->
                notificationService.getNotificationById(id, false));
    }
}
