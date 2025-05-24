package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Room;
import it.unimol.newunimol.model.RoomPUT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoomService {

    @PersistenceContext
    private EntityManager entityManager;

    public String addRoom(Room room) {
        entityManager.persist(room);
        return room.getIdAula();
    }

    public Room getRoomById(String idAula) {
        return entityManager.find(Room.class, idAula);
    }

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

    public List<Room> getAllRooms() {
        return entityManager.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }

    public void deleteRoom(String idAula) {
        Room room = entityManager.find(Room.class, idAula);
        if (room != null) {
            entityManager.remove(room);
        }
    }
}
