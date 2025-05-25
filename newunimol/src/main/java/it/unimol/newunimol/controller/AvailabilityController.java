package it.unimol.newunimol.controller;

import it.unimol.newunimol.model.Availability;
import it.unimol.newunimol.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @Autowired
    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/all")
    public List<Availability> getAllAvailabilities() {
        return availabilityService.getAllAvailabilities();
    }

    @GetMapping("/find/{idUtente}")
    public Availability getAvailabilityById(@PathVariable String idUtente) {
        return availabilityService.getAvailabilityById(idUtente)
                .orElse(null);
    }

    @PostMapping("/create")
    public Availability addAvailability(@RequestBody Availability availability) {
        return availabilityService.addAvailability(availability);
    }

    @PutMapping("/update/{idUtente}")
    public Availability updateAvailability(@PathVariable String idUtente, @RequestBody Availability updated) {
        return availabilityService.updateAvailability(idUtente, updated);
    }

    @DeleteMapping("/delete/{idUtente}")
    public void deleteAvailability(@PathVariable String idUtente) {
        availabilityService.deleteAvailability(idUtente);
    }
}