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


    @Value("${rabbitmq.exchange.assessments}")
    private String assessmentsExchange;

    //code per Room
    @Value("${rabbitmq.queue.room.created:room.created.queue}")
    private String roomCreatedQueue;
    @Value("${rabbitmq.queue.room.updated:room.updated.queue}")
    private String roomUpdatedQueue;
    @Value("${rabbitmq.queue.room.deleted:room.deleted.queue}")
    private String roomDeletedQueue;

    //code per Lecture
    @Value("${rabbitmq.queue.lecture.created:lecture.created.queue}")
    private String lectureCreatedQueue;
    @Value("${rabbitmq.queue.lecture.updated:lecture.updated.queue}")
    private String lectureUpdatedQueue;
    @Value("${rabbitmq.queue.lecture.deleted:lecture.deleted.queue}")
    private String lectureDeletedQueue;

    //code per Availability
    @Value("${rabbitmq.queue.availability.created:availability.created.queue}")
    private String availabilityCreatedQueue;
    @Value("${rabbitmq.queue.availability.updated:availability.updated.queue}")
    private String availabilityUpdatedQueue;
    @Value("${rabbitmq.queue.availability.deleted:availability.deleted.queue}")
    private String availabilityDeletedQueue;

    //configurazione per le deadLetter
    @Value("${rabbitmq.exchange.dlx:assessments.dlx}")
    private String deadLetterExchange;
    @Value("${rabbitmq.queue.dlq:assessments.dlq}")
    private String deadLetterQueue;

    //Time to live
    @Value("${rabbitmq.message.ttl:86400000}")
    private int messageTTL;

    //Exchange
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


    //code per Room
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

    //code per Lecture
    @Bean
    public Queue lectureCreatedQueue() {
        return QueueBuilder
                .durable(lectureCreatedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    @Bean
    public Queue lectureUpdatedQueue() {
        return QueueBuilder
                .durable(lectureUpdatedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    @Bean
    public Queue lectureDeletedQueue() {
        return QueueBuilder
                .durable(lectureDeletedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    //code per Availability
    @Bean
    public Queue availabilityCreatedQueue() {
        return QueueBuilder
                .durable(availabilityCreatedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    @Bean
    public Queue availabilityUpdatedQueue() {
        return QueueBuilder
                .durable(availabilityUpdatedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }

    @Bean
    public Queue availabilityDeletedQueue() {
        return QueueBuilder
                .durable(availabilityDeletedQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .withArgument("x-message-ttl", messageTTL)
                .build();
    }


    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(deadLetterQueue)
                .build();
    }

    //Bindings
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
    public Binding lectureCreatedBinding() {
        return BindingBuilder
                .bind(lectureCreatedQueue())
                .to(assessmentsExchange())
                .with("lecture.created");
    }

    @Bean
    public Binding lectureUpdatedBinding() {
        return BindingBuilder
                .bind(lectureUpdatedQueue())
                .to(assessmentsExchange())
                .with("lecture.updated");
    }

    @Bean
    public Binding lectureDeletedBinding() {
        return BindingBuilder
                .bind(lectureDeletedQueue())
                .to(assessmentsExchange())
                .with("lecture.deleted");
    }

    @Bean
    public Binding availabilityCreatedBinding() {
        return BindingBuilder
                .bind(availabilityCreatedQueue())
                .to(assessmentsExchange())
                .with("availability.created");
    }

    @Bean
    public Binding availabilityUpdatedBinding() {
        return BindingBuilder
                .bind(availabilityUpdatedQueue())
                .to(assessmentsExchange())
                .with("availability.updated");
    }

    @Bean
    public Binding availabilityDeletedBinding() {
        return BindingBuilder
                .bind(availabilityDeletedQueue())
                .to(assessmentsExchange())
                .with("availability.deleted");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dlq");
    }


    //converte il messaggio in json
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
                System.err.println("Messaggio non spedito: " + cause);
            }
        });
        template.setReturnsCallback(returned -> {
            System.err.println("Messaggio ritornato " + returned.getMessage());
        });
        template.setMandatory(true);
        return template;
    }
}