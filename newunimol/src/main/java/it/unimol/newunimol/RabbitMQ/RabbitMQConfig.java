package it.unimol.newunimol.RabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // === VALUES ===

    @Value("${rabbitmq.exchange.assessments}")
    private String assessmentsExchange;

    // Room Queues
    @Value("${rabbitmq.queue.room.created:room.created.queue}")
    private String roomCreatedQueue;
    @Value("${rabbitmq.queue.room.updated:room.updated.queue}")
    private String roomUpdatedQueue;
    @Value("${rabbitmq.queue.room.deleted:room.deleted.queue}")
    private String roomDeletedQueue;

    // Dead Letter Queue configuration
    @Value("${rabbitmq.exchange.dlx:assessments.dlx}")
    private String deadLetterExchange;
    @Value("${rabbitmq.queue.dlq:assessments.dlq}")
    private String deadLetterQueue;

    // TTL Configuration
    @Value("${rabbitmq.message.ttl:86400000}")
    private int messageTTL;

    // === EXCHANGES ===

    @Bean
    public TopicExchange assessmentsExchange() {
        return ExchangeBuilder
                .topicExchange(assessmentsExchange)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder
                .directExchange(deadLetterExchange)
                .durable(true)
                .build();
    }

    // === QUEUES ===

    // Room Queues
    @Bean
    public Queue roomCreatedQueue() {
        return QueueBuilder
                .durable(roomCreatedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    @Bean
    public Queue roomUpdatedQueue() {
        return QueueBuilder
                .durable(roomUpdatedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    @Bean
    public Queue roomDeletedQueue() {
        return QueueBuilder
                .durable(roomDeletedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    // Dead Letter Queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(deadLetterQueue)
                .build();
    }

    // === BINDINGS ===

    @Bean
    public Binding roomCreatedBinding() {
        return BindingBuilder
                .bind(roomCreatedQueue())
                .to(assessmentsExchange())
                .with("room.created");
    }

    @Bean
    public Binding roomUpdatedBinding() {
        return BindingBuilder
                .bind(roomUpdatedQueue())
                .to(assessmentsExchange())
                .with("room.updated");
    }

    @Bean
    public Binding roomDeletedBinding() {
        return BindingBuilder
                .bind(roomDeletedQueue())
                .to(assessmentsExchange())
                .with("room.deleted");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dlq");
    }

    // === MISC ===

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("Message not delivered: " + cause);
            }
        });
        template.setReturnsCallback(returned -> {
            System.err.println("Message returned: " + returned.getMessage());
        });
        template.setMandatory(true);
        return template;
    }
}