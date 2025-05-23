package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.Room;
import it.unimol.newunimol.model.RoomPUT;
import it.unimol.newunimol.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Creazione di una stanza
    @PostMapping("/create_room")
    public String addRoom(@RequestBody Room newRoom) {
        return roomService.addRoom(newRoom);
    }

    // Recupera una stanza per ID
    @GetMapping("/find/{id}")
    public Room getRoomById(@PathVariable String id) {
        return roomService.getRoomById(id);
    }

    // Aggiorna una stanza
    @PutMapping("/update/{id}")
    public String updateRoom(@PathVariable String id, @RequestBody RoomPUT updatedRoom) {
        return roomService.updateRoom(id, updatedRoom);
    }

    // Recupera tutte le stanze
    @GetMapping("/all")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Elimina una stanza
    @DeleteMapping("/delete/{id}")
    public void deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
    }
}