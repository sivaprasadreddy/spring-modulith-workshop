package com.sivalabs.bookstore.config;

import tools.jackson.databind.json.JsonMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "BookStoreExchange";

    public static final String ROUTING_KEY = "orders.new";

    public static final String QUEUE_NAME = "new-orders";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue newOrdersQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    Binding newOrdersQueueBinding(Queue newOrdersQueue, TopicExchange exchange) {
        return BindingBuilder.bind(newOrdersQueue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JsonMapper jsonMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJacksonMessageConverter(jsonMapper));
        return rabbitTemplate;
    }

    @Bean
    JacksonJsonMessageConverter producerJacksonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
