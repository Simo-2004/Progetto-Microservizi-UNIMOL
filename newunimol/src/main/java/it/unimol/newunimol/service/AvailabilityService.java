package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Availability;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvailabilityService {

    @PersistenceContext
    private EntityManager entityManager;

    // Recupera tutte le disponibilità
    public List<Availability> getAllAvailabilities() {
        return entityManager.createQuery("SELECT a FROM Availability a", Availability.class)
                .getResultList();
    }

    // Recupera una disponibilità per idUtente
    public Optional<Availability> getAvailabilityById(String idUtente) {
        Availability availability = entityManager.find(Availability.class, idUtente);
        return availability != null ? Optional.of(availability) : Optional.empty();
    }

    // Aggiunge una nuova disponibilità
    public Availability addAvailability(Availability availability) {
        entityManager.persist(availability);
        return availability;
    }

    // Elimina una disponibilità
    public boolean deleteAvailability(String idUtente) {
        return getAvailabilityById(idUtente).map(availability -> {
            entityManager.remove(availability);
            return true;
        }).orElse(false);
    }

    // Aggiorna una disponibilità
    public Availability updateAvailability(String idUtente, Availability updated) {
        return getAvailabilityById(idUtente).map(existing -> {
            existing.setDisponibile(updated.isDisponibile());
            existing.setData(updated.getData());
            existing.setOra_inizio(updated.getOra_inizio());
            existing.setOra_fine(updated.getOra_fine());
            existing.setNome_utente(updated.getNome_utente());
            existing.setCognome_utente(updated.getCognome_utente());

            return entityManager.merge(existing); // Salva le modifiche
        }).orElse(null);
    }
}