package com.example.coursework.service;

import com.example.coursework.model.Notification;
import com.example.coursework.model.Task;
import com.example.coursework.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.coursework.config.RabbitMQConfig;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;

    public Task createTask(String userId, String title, LocalDateTime dueDate) {
        Task task = Task.builder()
                .userId(userId)
                .title(title)
                .createdAt(LocalDateTime.now())
                .dueDate(dueDate)
                .resolved(false)
                .deleted(false)
                .build();

        Task savedTask = taskRepository.save(task);

        evictCachesByUserId(userId);

        // Отправляем уведомление в очередь
        rabbitTemplate.convertAndSend(RabbitMQConfig.TASK_EXCHANGE, RabbitMQConfig.TASK_ROUTING_KEY, savedTask);

        return savedTask;
    }

    @Cacheable(value = "tasksByUser", key = "#userId")
    public List<Task> getTasks(String userId){
        return taskRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Cacheable(value = "pendingTasksByUser", key = "#userId")
    public List<Task> getPendingTasks(String userId){
        return taskRepository.findByUserIdAndResolvedFalseAndDeletedFalse(userId);
    }

    @CacheEvict(value = {"tasksByUser", "pendingTasksByUser"}, key = "#task.userId")
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public void markDeleted(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Задача с ID " + id + " не найдена"));

        task.setDeleted(true);
        taskRepository.save(task);

        // Инвалидируем кэш для этого пользователя
        evictCachesByUserId(task.getUserId());
    }

    // Помощник для удаления кэша по userId
    @CacheEvict(value = {"tasksByUser", "pendingTasksByUser"}, key = "#userId")
    public void evictCachesByUserId(String userId) {
        // Метод пустой — он только для вызова @CacheEvict
    }

    /**
     * Метод, который каждые 60 секунд проверяет просроченные задачи
     */
    @Scheduled(fixedRate = 60000)
    public void checkOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepository.findByResolvedFalseAndDeletedFalseAndDueDateBefore(now);

        for (Task task : overdueTasks) {
            sendOverdueNotificationAsync(task);
        }
    }

    @Async
    public void sendOverdueNotificationAsync(Task task) {
        String message = "Задача просрочена: " + task.getTitle();

        Notification notification = Notification.builder()
                .userId(task.getUserId())
                .message(message)
                .received(false)
                .build();

        notificationService.addNotification(notification);
    }
}
