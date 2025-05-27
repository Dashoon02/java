package com.example.coursework.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String userId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private boolean resolved;
    private boolean deleted;

    @Version
    private Long version;  // <-- добавляем поле версии для оптимистичной блокировки
}
