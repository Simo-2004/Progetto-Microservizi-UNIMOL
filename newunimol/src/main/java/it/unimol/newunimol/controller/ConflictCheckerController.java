package it.unimol.newunimol.controller;

import it.unimol.newunimol.service.ConflictCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/conflicts")
public class ConflictCheckerController {

    @Autowired
    private ConflictCheckerService conflictCheckerService;

    @GetMapping("/check_room")
    public boolean checkRoomAvailability(
            @RequestParam String idAula,
            @RequestParam LocalDate data,
            @RequestParam LocalTime oraInizio,
            @RequestParam LocalTime oraFine) {
        return conflictCheckerService.isRoomAvailable(idAula, data, oraInizio, oraFine);
    }

    @GetMapping("/check_teacher")
    public boolean checkTeacherAvailability(
            @RequestParam String idDocente,
            @RequestParam LocalDate data,
            @RequestParam LocalTime oraInizio,
            @RequestParam LocalTime oraFine) {
        return conflictCheckerService.isTeacherAvailable(idDocente, data, oraInizio, oraFine);
    }
}