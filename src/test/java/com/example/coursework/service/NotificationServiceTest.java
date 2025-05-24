package com.example.coursework.service;

import com.example.coursework.model.Notification;
import com.example.coursework.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification sampleNotification;

    @BeforeEach
    void setUp() {
        sampleNotification = Notification.builder()
                .id(UUID.randomUUID())
                .userId("user1")
                .message("Test message")
                .createdAt(LocalDateTime.now())
                .received(false)
                .build();
    }

    @Test
    void testAddNotification_assignsIdAndCreatedAt() {
        Notification newNotification = Notification.builder()
                .userId("user1")
                .message("Hello")
                .build();

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification arg = invocation.getArgument(0);
            arg.setId(UUID.randomUUID());
            return arg;
        });

        Notification saved = notificationService.addNotification(newNotification);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testGetAll_returnsOnlyUserNotifications() {
        List<Notification> notifications = List.of(sampleNotification);
        when(notificationRepository.findByUserId("user1")).thenReturn(notifications);

        List<Notification> result = notificationService.getAll("user1");

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserId());
        verify(notificationRepository).findByUserId("user1");
    }

    @Test
    void testGetNotificationById_returnsCorrectNotification() {
        UUID id = sampleNotification.getId();
        when(notificationRepository.findById(id)).thenReturn(Optional.of(sampleNotification));

        List<Notification> result = notificationService.getNotificationById(id, false);

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void testGetNotificationById_withBeforeFlag() {
        UUID id = sampleNotification.getId();

        Notification olderNotification = Notification.builder()
                .id(UUID.randomUUID())
                .userId("user1")
                .message("Older")
                .createdAt(sampleNotification.getCreatedAt().minusDays(1))
                .received(false)
                .build();

        List<Notification> allUserNotifications = List.of(sampleNotification, olderNotification);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(sampleNotification));
        when(notificationRepository.findByUserId("user1")).thenReturn(allUserNotifications);

        List<Notification> result = notificationService.getNotificationById(id, true);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()));
    }

    @Test
    void testGetNotificationById_throwsIfNotFound() {
        UUID id = UUID.randomUUID();
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> notificationService.getNotificationById(id, false));
    }
}