package com.example.coursework.service;

import com.example.coursework.model.Task;
import com.example.coursework.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, rabbitTemplate);
    }

    @Test
    void testCreateTask_createsValidTask() {
        // Подготовка
        String userId = "user123";
        String title = "New Task";
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);

        Task savedTask = Task.builder()
                .userId(userId)
                .title(title)
                .createdAt(LocalDateTime.now())
                .dueDate(dueDate)
                .resolved(false)
                .deleted(false)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Действие
        Task result = taskService.createTask(userId, title, dueDate);

        // Проверка
        assertNotNull(result);
        assertEquals(title, result.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(Task.class));
    }
}