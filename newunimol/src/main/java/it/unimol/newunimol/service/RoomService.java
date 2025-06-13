package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Room;
import it.unimol.newunimol.model.RoomPUT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servizio per la gestione delle aule (Room) nel sistema.
 * Fornisce funzionalità di creazione, lettura, aggiornamento e cancellazione (CRUD).
 * Utilizza JPA per l'accesso al database tramite un EntityManager.
 */

@Service
@Transactional
public class RoomService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Aggiunge una nuova aula al sistema.
     *
     * @param room L'oggetto Room da salvare nel database.
     * @return L'identificatore dell'aula appena aggiunta.
     */
    public String addRoom(Room room) {
        entityManager.persist(room);
        return room.getIdAula();
    }

    /**
     * Recupera un'aula in base al suo id.
     *
     * @param idAula Identificatore dell'aula.
     * @return L'oggetto Room corrispondente, o null se non trovato.
     */
    public Room getRoomById(String idAula) {
        return entityManager.find(Room.class, idAula);
    }

    /**
     * Aggiorna un'aula esistente con i nuovi dati forniti.
     *
     * @param idAula Identificatore dell'aula da aggiornare.
     * @param updatedRoom Oggetto contenente i nuovi dati dell'aula.
     * @return L'identificatore dell'aula aggiornata, o null se non esiste.
     */
    public String updateRoom(String idAula, RoomPUT updatedRoom) {
        Room existing = entityManager.find(Room.class, idAula);
        if (existing != null) {
            existing.setNome(updatedRoom.getNome());
            existing.setEdificio(updatedRoom.getEdificio());
            existing.setCapienza(updatedRoom.getCapienza());
            existing.setDisponibile(updatedRoom.isDisponibile());
            existing.setDotazioni(updatedRoom.getDotazioni());
            entityManager.merge(existing);
            return idAula;
        }
        return null;
    }

    /**
     * Recupera tutte le aule presenti nel database.
     *
     * @return Lista di oggetti Room.
     */
    public List<Room> getAllRooms() {
        return entityManager.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }

    /**
     * Elimina un'aula dal sistema, se esiste.
     *
     * @param idAula Identificatore dell'aula da eliminare.
     */
    public void deleteRoom(String idAula) {
        Room room = entityManager.find(Room.class, idAula);
        if (room != null) {
            entityManager.remove(room);
        }
    }
}
