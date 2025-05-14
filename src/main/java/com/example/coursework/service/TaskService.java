package com.example.coursework.service;

import com.example.coursework.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    // ключ - ID задачи: значение - сама задача
    private final Map<UUID, Task> tasks = new HashMap<>();

    public Task createTask(Task task){
        /**
         * метод создаем новую задачу с уникальным ID,
         * так же присваивает ей время
         * и затем сохраняет ее в хранилище
         */
        UUID id = UUID.randomUUID(); // создаем уникальный идентификторв
        task.setId(id); // присваиваем созданный идентификатор задаче
        task.setCreatedAt(LocalDateTime.now()); // время создания
        tasks.put(id, task); // сохраняем в HashMap
        return task;
    }

    public List<Task> getTasks(String userId){
        /**
         * метод возвращает все задачи пользователя, кроме удаленных:
         * создаем поток значений, фильтруем и собираем резульат в список
         */
        return tasks.values().stream()
                .filter(t-> t.getUserId().equals(userId) && !t.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Task> getPendingTasks(String userId){
        /**
         * возвращаем только нерешенные задачи
         */
        return tasks.values().stream()
                .filter(t-> t.getUserId().equals(userId) && !t.isResolved() && !t.isDeleted())
                .collect(Collectors.toList());
    }

    public void markDeleted(UUID id){
        if (tasks.containsKey(id)) {
            tasks.get(id).setDeleted(true);
        }
    }
}
