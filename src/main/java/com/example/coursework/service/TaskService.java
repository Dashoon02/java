package com.example.coursework.service;

import com.example.coursework.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    // ключ - ID задачи: значение - сама задача
    private final Map<UUID, Task> tasks = new HashMap<>();

    /**
     * метод создаем новую задачу с уникальным ID,
     * присваивает ей время
     * и затем сохраняет эту задачу в хранилище
     */
    public Task createTask(String userId, String title, LocalDateTime dueDate) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .title(title)
                .createdAt(LocalDateTime.now())
                .dueDate(dueDate)
                .resolved(false)
                .deleted(false)
                .build();
        tasks.put(task.getId(), task);
        return task;
    }

    /**
     * метод возвращает все задачи пользователя, кроме удаленных:
     * создаем поток значений, фильтруем и собираем резульат в список
     */
    public List<Task> getTasks(String userId){
        return tasks.values().stream()
                .filter(t-> t.getUserId().equals(userId) && !t.isDeleted())
                .collect(Collectors.toList());
    }

    // метод возвращает только нерешенные задачи
    public List<Task> getPendingTasks(String userId){
        return tasks.values().stream()
                .filter(t-> t.getUserId().equals(userId) && !t.isResolved() && !t.isDeleted())
                .collect(Collectors.toList());
    }

    // метод помечает задачу как удаленную.
    public void markDeleted(UUID id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NoSuchElementException("Задача с ID " + id + " не найдена");
        }
        task.setDeleted(true);
    }
}
