package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Availability;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    private final List<Availability> availabilities = new ArrayList<>();

    public List<Availability> getAllAvailabilities() {
        return new ArrayList<>(availabilities);
    }

    public Optional<Availability> getAvailabilityById(String idUtente) {
        return availabilities.stream()
                .filter(a -> a.getIdUtente().equals(idUtente))
                .findFirst();
    }

    public Availability addAvailability(Availability availability) {
        this.availabilities.add(availability);
        return availability;
    }

    public boolean deleteAvailability(String idUtente) {
        return availabilities.removeIf(a -> a.getIdUtente().equals(idUtente));
    }

    public Availability updateAvailability(String idUtente, Availability updated) {
        Optional<Availability> optional = getAvailabilityById(idUtente);
        if (optional.isPresent()) {
            Availability availability = optional.get();
            availability.setDisponibile(updated.isDisponibile());
            availability.setData(updated.getData());
            availability.setOra_inizio(updated.getOra_inizio());
            availability.setOra_fine(updated.getOra_fine());
            // aggiorna anche nome_utente e cognome_utente se i getter/setter ci sono
            return availability;
        }
        return null;
    }
}