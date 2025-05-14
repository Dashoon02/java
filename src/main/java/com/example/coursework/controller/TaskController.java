package com.example.coursework.controller;

import com.example.coursework.model.Task;
import com.example.coursework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return ResponseEntity.status(201).body(taskService.createTask(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.markDeleted(id);
        return ResponseEntity.noContent().build();
    }
}
