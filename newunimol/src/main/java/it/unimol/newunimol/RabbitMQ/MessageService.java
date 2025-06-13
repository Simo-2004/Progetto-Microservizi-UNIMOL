package it.unimol.newunimol.RabbitMQ;

import it.unimol.newunimol.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.assessments}")
    private String assessmentsExchange;

    @Value("${microservice.name:microservice-assessment-feedback}")
    private String serviceName;

    // === ROOM EVENTS ===

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishRoomCreated(Room room) {
        try {
            Map<String, Object> message = createRoomMessage(room, "ROOM_CREATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "room.created", message);
            logger.info("Room created event published successfully for room ID: {}", room.getIdAula());
        } catch (Exception e) {
            logger.error("Error publishing room created event for room ID: {}", room.getIdAula(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishRoomUpdated(Room room) {
        try {
            Map<String, Object> message = createRoomMessage(room, "ROOM_UPDATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "room.updated", message);
            logger.info("Room updated event published successfully for room ID: {}", room.getIdAula());
        } catch (Exception e) {
            logger.error("Error publishing room updated event for room ID: {}", room.getIdAula(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishRoomDeleted(String roomId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("eventType", "ROOM_DELETED");
            message.put("roomId", roomId);
            message.put("serviceName", serviceName);
            message.put("timestamp", System.currentTimeMillis());
            rabbitTemplate.convertAndSend(assessmentsExchange, "room.deleted", message);
            logger.info("Room deleted event published successfully for room ID: {}", roomId);
        } catch (Exception e) {
            logger.error("Error publishing room deleted event for room ID: {}", roomId, e);
            throw e;
        }
    }

    // === HELPER METHODS ===

    private Map<String, Object> createRoomMessage(Room room, String eventType) {
        Map<String, Object> message = new HashMap<>();
        message.put("eventType", eventType);
        message.put("roomId", room.getIdAula());
        message.put("nome", room.getNome());
        message.put("edificio", room.getEdificio());
        message.put("capienza", room.getCapienza());
        message.put("disponibile", room.isDisponibile());
        message.put("dotazioni", room.getDotazioni());
        message.put("serviceName", serviceName);
        message.put("timestamp", System.currentTimeMillis());
        return message;
    }

}