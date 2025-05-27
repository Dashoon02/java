package com.example.coursework.service;

import com.example.coursework.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.coursework.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * метод создаем новую задачу с уникальным ID,
     * присваивает ей время
     * и затем сохраняет эту задачу в хранилище
     */
    public Task createTask(String userId, String title, LocalDateTime dueDate) {
        Task task = Task.builder()
                .userId(userId)
                .title(title)
                .createdAt(LocalDateTime.now())
                .dueDate(dueDate)
                .resolved(false)
                .deleted(false)
                .build();

        return taskRepository.save(task);
    }

    /**
     * метод возвращает все задачи пользователя, кроме удаленных:
     * создаем поток значений, фильтруем и собираем резульат в список
     */
    public List<Task> getTasks(String userId){
        return taskRepository.findByUserIdAndDeletedFalse(userId);
    }

    // метод возвращает только нерешенные задачи
    public List<Task> getPendingTasks(String userId){
        return taskRepository.findByUserIdAndResolvedFalseAndDeletedFalse(userId);
    }

    // метод помечает задачу как удаленную.
    public void markDeleted(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Задача с ID " + id + " не найдена"));

        task.setDeleted(true);
        taskRepository.save(task);
    }
}
