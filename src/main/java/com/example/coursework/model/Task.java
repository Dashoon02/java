package com.example.coursework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private UUID id;
    private String userId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private boolean resolved;
    private boolean deleted;
}

