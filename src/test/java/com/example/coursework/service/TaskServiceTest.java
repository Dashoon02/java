package com.example.coursework.service;

import com.example.coursework.model.Task;
import com.example.coursework.repository.TaskRepository;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private final String userId = "user1";
    private final LocalDateTime dueDate = LocalDateTime.now().plusDays(1);

    @Test
    void testCreateTask_createsValidTask() {
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task task = taskService.createTask(userId, "Test Task", dueDate);

        assertNotNull(task);
        assertEquals(userId, task.getUserId());
        assertEquals("Test Task", task.getTitle());
        assertFalse(task.isDeleted());
        assertFalse(task.isResolved());
    }

    @Test
    void testGetTasks_returnsOnlyNonDeleted() {
        Task t1 = Task.builder().id(UUID.randomUUID()).userId(userId).title("Task 1").deleted(false).build();
        Task t2 = Task.builder().id(UUID.randomUUID()).userId(userId).title("Task 2").deleted(false).build();

        when(taskRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(List.of(t1, t2));

        List<Task> tasks = taskService.getTasks(userId);

        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(t1));
        assertTrue(tasks.contains(t2));
    }

    @Test
    void testGetPendingTasks_returnsOnlyPendingAndNotDeleted() {
        Task t1 = Task.builder().id(UUID.randomUUID()).userId(userId).title("Task 1").resolved(false).deleted(false).build();
        Task t2 = Task.builder().id(UUID.randomUUID()).userId(userId).title("Task 2").resolved(true).deleted(false).build();

        when(taskRepository.findByUserIdAndResolvedFalseAndDeletedFalse(userId)).thenReturn(List.of(t1));

        List<Task> pending = taskService.getPendingTasks(userId);

        assertEquals(1, pending.size());
        assertEquals(t1.getId(), pending.get(0).getId());
    }

    @Test
    void testMarkDeleted_marksTaskAsDeleted() {
        UUID id = UUID.randomUUID();
        Task task = Task.builder().id(id).userId(userId).deleted(false).build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        taskService.markDeleted(id);

        assertTrue(task.isDeleted());
        verify(taskRepository).save(task);
    }

    @Test
    void testMarkDeleted_throwsIfNotFound() {
        UUID invalidId = UUID.randomUUID();
        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.markDeleted(invalidId));
    }
}
