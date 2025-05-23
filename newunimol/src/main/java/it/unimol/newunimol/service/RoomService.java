package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Room;
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

    public Room addRoom(Room room) {
        entityManager.persist(room);
        return room;
    }

    public Room getRoomById(String id) {
        return entityManager.find(Room.class, id);
    }

    public Room updateRoom(String id, Room updatedRoom) {
        Room existing = entityManager.find(Room.class, id);
        if (existing != null) {
            existing.setName(updatedRoom.getName());
            existing.setCapacity(updatedRoom.getCapacity());
            return entityManager.merge(existing);
        }
        return null;
    }

    public List<Room> getAllRooms() {
        return entityManager.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }

    public void deleteRoom(String id) {
        Room room = entityManager.find(Room.class, id);
        if (room != null) {
            entityManager.remove(room);
        }
    }
}
