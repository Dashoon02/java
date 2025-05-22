package com.example.coursework.service;

import com.example.coursework.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    /**
     * Проверяем, что создаётся задача с правильными значениями
     */
    @Test
    void testCreateTask_createsValidTask() {
        String userId = "user1";
        String title = "Test Task";
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);

        Task task = taskService.createTask(userId, title, dueDate);

        assertNotNull(task); // Проверяем, что задача не null
        assertEquals(userId, task.getUserId()); // Проверяем userId
        assertEquals(title, task.getTitle()); // Проверяем заголовок
        assertFalse(task.isDeleted()); // Новая задача не должна быть удалённой
        assertFalse(task.isResolved()); // И не должна быть выполненной
    }

    /**
     * Проверяем, что метод getTasks возвращает только не удалённые задачи
     */
    @Test
    void testGetTasks_returnsOnlyNonDeleted() {
        String userId = "user1";
        Task t1 = taskService.createTask(userId, "Task 1", LocalDateTime.now().plusDays(1));
        Task t2 = taskService.createTask(userId, "Task 2", LocalDateTime.now().plusDays(2));
        taskService.markDeleted(t1.getId()); // Помечаем первую задачу как удалённую

        List<Task> tasks = taskService.getTasks(userId);

        assertEquals(1, tasks.size()); // Ожидаем только одну задачу
        assertEquals(t2.getId(), tasks.get(0).getId()); // Это должна быть вторая задача
    }

    /**
     * Проверяем, что метод getPendingTasks возвращает только нерешённые и не удалённые задачи
     */
    @Test
    void testGetPendingTasks_returnsOnlyPendingAndNotDeleted() {
        String userId = "user1";
        Task t1 = taskService.createTask(userId, "Task 1", LocalDateTime.now().plusDays(1));
        Task t2 = taskService.createTask(userId, "Task 2", LocalDateTime.now().plusDays(1));
        t2.setResolved(true); // Помечаем вторую задачу как решённую

        List<Task> pending = taskService.getPendingTasks(userId);

        assertEquals(1, pending.size()); // Должна остаться одна задача
        assertEquals(t1.getId(), pending.get(0).getId()); // Это должна быть нерешённая первая задача
    }

    /**
     * Проверяем, что задача корректно помечается как удалённая
     */
    @Test
    void testMarkDeleted_marksTaskAsDeleted() {
        Task task = taskService.createTask("user", "Title", LocalDateTime.now().plusDays(1));

        taskService.markDeleted(task.getId());

        assertTrue(task.isDeleted()); // Задача должна быть удалена
    }

    /**
     * Проверяем, что при попытке удалить несуществующую задачу выбрасывается исключение
     */
    @Test
    void testMarkDeleted_throwsIfNotFound() {
        UUID invalidId = UUID.randomUUID();

        assertThrows(NoSuchElementException.class, () -> {
            taskService.markDeleted(invalidId);
        });
    }
}
