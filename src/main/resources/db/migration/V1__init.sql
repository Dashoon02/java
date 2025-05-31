CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       user_id VARCHAR(255),
                       title VARCHAR(255),
                       created_at TIMESTAMP,
                       due_date TIMESTAMP,
                       resolved BOOLEAN,
                       deleted BOOLEAN,
                       version INT DEFAULT 0,
                       CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE notifications (
                               id UUID PRIMARY KEY,
                               user_id VARCHAR(255),
                               message TEXT,
                               created_at TIMESTAMP,
                               received BOOLEAN,
                               CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id)
);
