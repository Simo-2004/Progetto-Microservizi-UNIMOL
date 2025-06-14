package it.unimol.newunimol.RabbitMQ;

import it.unimol.newunimol.model.Availability;
import it.unimol.newunimol.model.Lecture;
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

    @Value("${microservice.name:gestione-orari-e-aule}")
    private String serviceName;

    //eventi Room
    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000)) //gestione di problematiche delle code
    public void publishRoomCreated(Room room) {
        try {
            Map<String, Object> message = createRoomMessage(room, "ROOM_CREATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "room.created", message);
            logger.info("Stanza creata ed evento pubblicato con successo per Room ID: {}", room.getIdAula());
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di creazione Room con ID: {}", room.getIdAula(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishRoomUpdated(Room room) {
        try {
            Map<String, Object> message = createRoomMessage(room, "ROOM_UPDATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "room.updated", message);
            logger.info("Stanza aggiornata ed evento pubblicato con successo per Room ID: {}", room.getIdAula());
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di aggiornamento Room con ID: {}", room.getIdAula(), e);
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
            logger.info("Stanza eliminata ed evento pubblicato con successo per Room ID: {}", roomId);
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di eliminazione Room con ID: {}", roomId, e);
            throw e;
        }
    }

    //eventi Lecture
    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000)) //gestione di problematiche delle code
    public void publishLectureCreated(Lecture lecture) {
        try {
            Map<String, Object> message = createLectureMessage(lecture, "LECTURE_CREATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "lecture.created", message);
            logger.info("Lezione creata ed evento pubblicato con successo per Lecture ID: {}", lecture.getIdLezione());
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di creazione Lecture con ID: {}", lecture.getIdLezione(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishLectureUpdated(Lecture lecture) {
        try {
            Map<String, Object> message = createLectureMessage(lecture, "LECTURE_UPDATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "lecture.updated", message);
            logger.info("Lezione aggiornata ed evento pubblicato con successo per Lecture ID: {}", lecture.getIdLezione());
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di aggiornamento Lecture con ID: {}", lecture.getIdLezione(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishLectureDeleted(String lectureId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("eventType", "LECTURE_DELETED");
            message.put("lectureId", lectureId);
            message.put("serviceName", serviceName);
            message.put("timestamp", System.currentTimeMillis());
            rabbitTemplate.convertAndSend(assessmentsExchange, "lecture.deleted", message);
            logger.info("Lezione eliminata ed evento pubblicato con successo per Lecture ID: {}", lectureId);
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di eliminazione Lecture con ID: {}", lectureId, e);
            throw e;
        }
    }



    //eventi Availability
    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000)) //gestione di problematiche delle code
    public void publishAvailabilityCreated(Availability availability) {
        try {
            Map<String, Object> message = createAvailabilityMessage(availability, "AVAILABILITY_CREATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "availability.created", message);
            logger.info("Disponibilita' creata ed evento pubblicato con successo per availability ID: {}", availability.getIdUtente());
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di creazione availability con ID: {}", availability.getIdUtente(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishAvailabilityUpdated(Availability availability) {
        try {
            Map<String, Object> message = createAvailabilityMessage(availability, "AVAILABILITY_UPDATED");
            rabbitTemplate.convertAndSend(assessmentsExchange, "availability.updated", message);
            logger.info("Disponibilita' aggiornata ed evento pubblicato con successo per availability ID: {}", availability.getIdUtente());
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di aggiornamento availability con ID: {}", availability.getIdUtente(), e);
            throw e;
        }
    }

    @Retryable(value = {AmqpException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void publishAvailabilityDeleted(String availabilityID) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("eventType", "AVAILABILITY_DELETED");
            message.put("availabilityID", availabilityID);
            message.put("serviceName", serviceName);
            message.put("timestamp", System.currentTimeMillis());
            rabbitTemplate.convertAndSend(assessmentsExchange, "availability.deleted", message);
            logger.info("Disponibilita' eliminata ed evento pubblicato con successo per availability ID: {}", availabilityID);
        } catch (Exception e) {
            logger.error("Errore nella pubblicazione dell'evento di eliminazione availability con ID: {}", availabilityID, e);
            throw e;
        }
    }

    //metodi di creazione del messaggio
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

    private Map<String, Object> createLectureMessage(Lecture lecture, String eventType) {
        Map<String, Object> message = new HashMap<>();
        message.put("eventType", eventType);
        message.put("roomId", lecture.getIdAula());
        message.put("LectureID", lecture.getIdLezione());
        message.put("TeacherID", lecture.getDocenteId());
        message.put("Date", lecture.getData());
        message.put("Start Time", lecture.getOraInizio());
        message.put("End Time", lecture.getOraFine());
        message.put("Lesson name", lecture.getNomeLezione());
        message.put("serviceName", serviceName);
        message.put("timestamp", System.currentTimeMillis());
        return message;
    }

    //availability
    private Map<String, Object> createAvailabilityMessage(Availability availability, String eventType) {
        Map<String, Object> message = new HashMap<>();
        message.put("eventType", eventType);
        message.put("userId", availability.getIdUtente());
        message.put("date", availability.getData());
        message.put("start time", availability.getOra_inizio());
        message.put("end time", availability.getOra_fine());
        message.put("name", availability.getNome_utente());
        message.put("surname", availability.getCognome_utente());
        message.put("serviceName", serviceName);
        message.put("timestamp", System.currentTimeMillis());
        return message;
    }


}