package com.example.coursework.repository;

import com.example.coursework.model.Notification;
import com.example.coursework.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserId() {
        User user = new User();
        user.setId("notifyUser");
        user.setUsername("testuser");
        userRepository.save(user);

        Notification notification = new Notification();
        notification.setUserId(user.getId()); // Устанавливаем userId напрямую
        notification.setMessage("Test message");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceived(false);
        notificationRepository.save(notification);

        List<Notification> result = notificationRepository.findByUserId(user.getId());
        assertEquals(1, result.size());
        assertEquals("Test message", result.get(0).getMessage());
        assertEquals(user.getId(), result.get(0).getUserId());
    }
}
