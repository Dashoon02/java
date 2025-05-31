package com.example.coursework.controller;

import com.example.coursework.model.Task;
import com.example.coursework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<Task> getAll(@RequestParam String userId) {
        return taskService.getTasks(userId);
    }

    @GetMapping("/pending")
    public List<Task> getPending(@RequestParam String userId) {
        return taskService.getPendingTasks(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(
            @RequestParam String userId,
            @RequestParam String title,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate) {
        return taskService.createTask(userId, title, dueDate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable UUID id) {
        taskService.markDeleted(id);
    }
}
