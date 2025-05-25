Task Manager - A course project on Java Spring Boot

A task management application with notifications, caching, message queues, and a task scheduler.

Technology stack: Java 17+, Spring Boot 3, Spring Data JPA, PostgreSQL, Redis (caching), RabbitMQ (asynchronous notifications), Spring Scheduling + Async (background check for overdue tasks), Flyway (database migrations), Lombok.

Main features:
Creating, deleting, and viewing tasks. Storing tasks in PostgreSQL. Caching of Redis requests Asynchronous sending of notifications via RabbitMQ. A scheduler that periodically checks overdue tasks.. Automatic schema migration via Flyway. Coverage by unit tests (JUnit + Mockito).
