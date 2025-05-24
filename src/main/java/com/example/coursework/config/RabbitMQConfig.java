package com.example.coursework.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TASK_QUEUE = "taskQueue";
    public static final String TASK_EXCHANGE = "taskExchange";
    public static final String TASK_ROUTING_KEY = "task.created";

    @Bean
    public Queue queue() {
        return new Queue(TASK_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(TASK_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(TASK_ROUTING_KEY);
    }
}
