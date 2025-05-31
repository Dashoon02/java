package com.example.coursework.listener;

import com.example.coursework.model.Task;
import com.example.coursework.model.Notification;
import com.example.coursework.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskNotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "taskQueue")
    public void handleTaskCreated(Task task) {
        Notification notification = Notification.builder()
                .userId(task.getUserId())
                .message("Новая задача: " + task.getTitle())
                .build();

        notificationService.addNotification(notification);
    }
}

