package com.example.coursework.repository;

import com.example.coursework.model.Task;
import com.example.coursework.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserIdAndDeletedFalse() {
        String userId = "user123";

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser123");
        userRepository.save(user);

        Task t1 = Task.builder()
                .userId(userId)
                .title("T1")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        Task t2 = Task.builder()
                .userId(userId)
                .title("T2")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        Task t3 = Task.builder()
                .userId(userId)
                .title("T3")
                .deleted(true)
                .createdAt(LocalDateTime.now())
                .build();

        taskRepository.saveAll(List.of(t1, t2, t3));

        List<Task> result = taskRepository.findByUserIdAndDeletedFalse(userId);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> !t.isDeleted()));
    }

    @Test
    void testFindByUserIdAndResolvedFalseAndDeletedFalse() {
        String userId = "user456";

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser456");
        userRepository.save(user);

        Task t1 = Task.builder()
                .userId(userId)
                .title("Active")
                .resolved(false)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        Task t2 = Task.builder()
                .userId(userId)
                .title("Resolved")
                .resolved(true)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        Task t3 = Task.builder()
                .userId(userId)
                .title("Deleted")
                .resolved(false)
                .deleted(true)
                .createdAt(LocalDateTime.now())
                .build();

        taskRepository.saveAll(List.of(t1, t2, t3));

        List<Task> result = taskRepository.findByUserIdAndResolvedFalseAndDeletedFalse(userId);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isResolved());
        assertFalse(result.get(0).isDeleted());
    }
}
