package com.example.coursework.repository;

import com.example.coursework.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>{
    List<Task> findByUserIdAndDeletedFalse(String userId);
    List<Task> findByUserIdAndResolvedFalseAndDeletedFalse(String userId);
}
