package it.unimol.newunimol.controller;

import it.unimol.newunimol.RabbitMQ.MessageService;
import it.unimol.newunimol.model.Room;
import it.unimol.newunimol.service.RoomService;
import it.unimol.newunimol.service.TokenJWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TokenJWTService tokenJWTService;

    // Creazione di una stanza
    @PostMapping("/create_room")
    public ResponseEntity<String> addRoom(@RequestBody Room newRoom, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            messageService.publishRoomCreated(newRoom);
            return ResponseEntity.ok(roomService.addRoom(newRoom));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Recupera una stanza per ID
    @GetMapping("/find_room/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        if(roomService.getRoomById(id) == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok(roomService.getRoomById(id));
        }

    }


    // Aggiorna una stanza
    @PutMapping("/update_room/{id}")
    public ResponseEntity<String> updateRoom(@PathVariable String id, @RequestBody Room updatedRoom, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            messageService.publishRoomUpdated(updatedRoom);
            return ResponseEntity.ok(roomService.updateRoom(id, updatedRoom));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Recupera tutte le stanze
    @GetMapping("/all_room")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Elimina una stanza
    @DeleteMapping("/delete_room/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable String id, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            roomService.deleteRoom(id);
            messageService.publishRoomDeleted(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }



}