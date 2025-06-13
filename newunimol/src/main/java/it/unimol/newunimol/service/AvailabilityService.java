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

    /**
     * Recupera tutte le disponibilità da tutti i docenti
     *
     * @return Una lista di disponibilità
     */
    public List<Availability> getAllAvailabilities() {
        return entityManager.createQuery("SELECT a FROM Availability a", Availability.class)
                .getResultList();
    }

    /**
     * Cerca e restituisce l'oggetto Availability associato all'id dell'utente specificato.
     *
     * @param idUtente L'identificatore univoco dell'utente di cui si vuole recuperare la disponibilità.
     * @return Un oggetto Optional contenente la disponibilità trovata se esistente.
     * Altrimenti, restituisce un oggetto Optional vuoto.
     */
    public Optional<Availability> getAvailabilityById(String idUtente) {
        Availability availability = entityManager.find(Availability.class, idUtente);
        return availability != null ? Optional.of(availability) : Optional.empty();
    }

    /**
     * Aggiunge una nuova disponibilità nel database.
     *
     * @param availability L'oggetto Availability da salvare.
     * @return L'oggetto Availability appena salvato.
     */
    public Availability addAvailability(Availability availability) {
        entityManager.persist(availability);
        return availability;
    }

    /**
     * Elimina una disponibilità esistente in base all'id dell'utente.
     *
     * @param idUtente L'identificatore univoco dell'utente di cui si vuole eliminare la disponibilità.
     * @return
     * true se la disponibilità è stata trovata ed eliminata.
     * false altrimenti.
     */
    public boolean deleteAvailability(String idUtente) {
        return getAvailabilityById(idUtente).map(availability -> {
            entityManager.remove(availability);
            return true;
        }).orElse(false);
    }

    /**
     * Aggiorna una disponibilità esistente con nuovi dati.
     *
     * @param idUtente L'identificatore univoco dell'utente di cui aggiornare la disponibilità.
     * @param updated L'oggetto Availability contenente i nuovi dati.
     * @return L'oggetto Availability aggiornato, se esiste.
     * Altrimenti null.
     */
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