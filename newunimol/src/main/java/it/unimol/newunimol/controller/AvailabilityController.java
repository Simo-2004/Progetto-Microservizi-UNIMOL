package it.unimol.newunimol.controller;

import it.unimol.newunimol.RabbitMQ.MessageService;
import it.unimol.newunimol.model.Availability;
import it.unimol.newunimol.service.AvailabilityService;
import it.unimol.newunimol.service.TokenJWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/availability")
public class AvailabilityController {

    @Autowired
    private MessageService messageService;

    private final AvailabilityService availabilityService;

    @Autowired
    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Autowired
    private TokenJWTService tokenJWTService;

    @GetMapping("/all_availability")
    public ResponseEntity<List<Availability>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }

    @GetMapping("/find_availability/{idUtente}")
    public ResponseEntity<Availability> getAvailabilityById(@PathVariable String idUtente) {

        if(availabilityService.getAvailabilityById(idUtente).orElse(null) == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok(availabilityService.getAvailabilityById(idUtente).orElse(null));
        }

    }

    @PostMapping("/create_availability")
    public ResponseEntity<Availability> addAvailability(@RequestBody Availability availability, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            messageService.publishAvailabilityCreated(availability);
            return ResponseEntity.ok(availabilityService.addAvailability(availability));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update_availability/{idUtente}")
    public ResponseEntity<Availability> updateAvailability(@PathVariable String idUtente, @RequestBody Availability updated, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            messageService.publishAvailabilityUpdated(updated);
            return ResponseEntity.ok(availabilityService.updateAvailability(idUtente, updated));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete_availability/{idUtente}")
    public ResponseEntity<?> deleteAvailability(@PathVariable String idUtente, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        String role = tokenJWTService.extractRole(token);

        if("ADMIN".equals(role) || "SADMIN".equals(role)) {
            messageService.publishAvailabilityDeleted(idUtente);
            availabilityService.deleteAvailability(idUtente);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}