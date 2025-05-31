package com.example.coursework.controller;

import com.example.coursework.model.Task;
import com.example.coursework.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TaskControllerTest {

    @Mock
    private TaskService taskService;

    private TaskController taskController;

    private Task task1;
    private Task task2;

    /**
     * Подготовка тестового окружения, создание моков и тестовых задач.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskController = new TaskController(taskService);

        task1 = Task.builder()
                .id(UUID.randomUUID())
                .userId("user1")
                .title("Task 1")
                .createdAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(1))
                .resolved(false)
                .deleted(false)
                .build();

        task2 = Task.builder()
                .id(UUID.randomUUID())
                .userId("user1")
                .title("Task 2")
                .createdAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(2))
                .resolved(false)
                .deleted(false)
                .build();
    }

    /**
     * Проверяем, что метод getAll возвращает все задачи пользователя.
     */
    @Test
    void getAll_ShouldReturnAllTasks() {
        List<Task> expectedTasks = List.of(task1, task2);
        when(taskService.getTasks("user1")).thenReturn(expectedTasks);

        List<Task> actualTasks = taskController.getAll("user1");

        assertEquals(expectedTasks, actualTasks); // Проверка соответствия результата ожиданиям
        verify(taskService, times(1)).getTasks("user1"); // Убедиться, что метод сервиса вызван один раз
    }

    /**
     * Проверяем, что метод getPending возвращает только нерешённые задачи пользователя.
     */
    @Test
    void getPending_ShouldReturnPendingTasks() {
        List<Task> expectedTasks = List.of(task1);
        when(taskService.getPendingTasks("user1")).thenReturn(expectedTasks);

        List<Task> actualTasks = taskController.getPending("user1");

        assertEquals(expectedTasks, actualTasks); // Проверка списка нерешённых задач
        verify(taskService, times(1)).getPendingTasks("user1"); // Убедиться, что сервис вызван корректно
    }

    /**
     * Проверяем, что метод create создаёт новую задачу и возвращает её.
     */
    @Test
    void create_ShouldReturnCreatedTask() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(3);
        Task createdTask = Task.builder()
                .id(UUID.randomUUID())
                .userId("user1")
                .title("New Task")
                .createdAt(LocalDateTime.now())
                .dueDate(dueDate)
                .resolved(false)
                .deleted(false)
                .build();

        when(taskService.createTask("user1", "New Task", dueDate)).thenReturn(createdTask);

        Task actualTask = taskController.create("user1", "New Task", dueDate);

        assertNotNull(actualTask); // Убедиться, что задача создана
        assertEquals("New Task", actualTask.getTitle()); // Проверка заголовка
        assertEquals(dueDate, actualTask.getDueDate()); // Проверка срока выполнения
        verify(taskService, times(1)).createTask("user1", "New Task", dueDate); // Проверка вызова сервиса
    }

    /**
     * Проверяем, что метод deleteTask вызывает соответствующий метод сервиса для удаления задачи.
     */
    @Test
    void deleteTask_ShouldCallServiceOnce() {
        UUID taskId = UUID.randomUUID();
        doNothing().when(taskService).markDeleted(taskId);

        taskController.deleteTask(taskId); // Метод не возвращает значение

        verify(taskService, times(1)).markDeleted(taskId); // Проверка вызова метода
    }
}
