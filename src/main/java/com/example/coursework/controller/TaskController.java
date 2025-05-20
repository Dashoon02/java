package com.example.coursework.controller;

import com.example.coursework.model.Task;
import com.example.coursework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable UUID id) {
        taskService.markDeleted(id);
        return "Задача удалена";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
