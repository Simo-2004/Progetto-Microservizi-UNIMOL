package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Lecture;
import it.unimol.newunimol.model.Availability;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servizio per la gestione delle lezioni (Lecture) nel sistema.
 * Fornisce funzionalità di creazione, lettura, aggiornamento e cancellazione (CRUD),
 * con validazioni avanzate per garantire che:
 * - Il docente sia disponibile nella data/ora richiesta,
 * - L'aula sia libera nello stesso orario,
 * - Non vi siano conflitti con altri impegni (lezioni o esami).
 * Utilizza JPA per l'accesso al database e si appoggia a servizi ausiliari come:
 * - {@link AvailabilityService} per verificare la disponibilità del docente,
 * - {@link ConflictCheckerService} per controllare eventuali sovrapposizioni.
 */

@Service
@Transactional
public class LectureService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AvailabilityService availabilityService; // Servizio per recuperare la disponibilità

    @Autowired
    private ConflictCheckerService conflictCheckerService;

    /**
     * Aggiunge una nuova lezione dopo aver effettuato tutte le verifiche necessarie:
     * - Disponibilità del docente,
     * - Disponibilità dell’aula,
     * - Assenza di conflitti con altri impegni (lezioni/esami).
     *
     * @param newLecture La lezione da inserire.
     * @return La lezione appena creata.
     * @throws RuntimeException se una delle verifiche fallisce.
     */
    public Lecture addLecture(Lecture newLecture) {
        String docenteId = newLecture.getDocenteId();

        // Recupera la disponibilità del docente
        Availability availability = availabilityService.getAvailabilityById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente non trovato"));

        // Verifica se è disponibile
        if (!availability.isDisponibile()) {
            throw new RuntimeException("Il docente non è disponibile.");
        }

        // Controllo disponibilità docente
        if (!conflictCheckerService.isTeacherAvailable(
                newLecture.getDocenteId(),
                newLecture.getData(),
                newLecture.getOraInizio(),
                newLecture.getOraFine())) {
            throw new RuntimeException("Il docente ha un impegno in quell'orario.");
        }

        // Controllo disponibilità aula
        if (!conflictCheckerService.isRoomAvailable(
                newLecture.getIdAula(),
                newLecture.getData(),
                newLecture.getOraInizio(),
                newLecture.getOraFine())) {
            throw new RuntimeException("L'aula non è disponibile in quell'orario.");
        }

        // Verifica che la data coincida
        if (!availability.getData().isEqual(newLecture.getData())) {
            throw new RuntimeException("La data della lezione non corrisponde alla disponibilità del docente.");
        }

        // Verifica che l'orario rientri nella disponibilità
        if (newLecture.getOraInizio().isBefore(availability.getOra_inizio()) ||
                newLecture.getOraFine().isAfter(availability.getOra_fine())) {
            throw new RuntimeException("L'orario della lezione non rientra nella disponibilità del docente.");
        }

        // Se tutto ok, salva la lezione
        entityManager.persist(newLecture);
        return newLecture;
    }

    /**
     * Recupera una lezione in base al suo identificatore univoco.
     *
     * @param id Identificatore della lezione.
     * @return L'oggetto Lecture corrispondente, o null se non trovato.
     */
    public Lecture getLectureById(String id) {
        return entityManager.find(Lecture.class, id);
    }

    /**
     * Aggiorna una lezione esistente con i nuovi dati forniti,
     * previa verifica della disponibilità del docente e dell’aula.
     *
     * @param id Identificatore della lezione da aggiornare.
     * @param updatedLecture Oggetto contenente i nuovi dati.
     * @return La lezione aggiornata, o null se non esiste.
     * @throws RuntimeException se una delle verifiche fallisce.
     */
    public Lecture updateLecture(String id, Lecture updatedLecture) {
        // 1. Cerca la lezione esistente
        Lecture existing = getLectureById(id);
        if (existing == null) {
            return null; // Non esiste, non si può aggiornare
        }

        // 2. Recupera la disponibilità del docente
        String docenteId = updatedLecture.getDocenteId();
        Availability availability = availabilityService.getAvailabilityById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente non trovato"));

        // 3. Verifica disponibilità
        if (!availability.isDisponibile()) {
            throw new RuntimeException("Il docente non è disponibile.");
        }

        // 4. Verifica data
        if (!availability.getData().isEqual(updatedLecture.getData())) {
            throw new RuntimeException("La data della lezione non corrisponde alla disponibilità del docente.");
        }

        // 5. Verifica orario
        if (updatedLecture.getOraInizio().isBefore(availability.getOra_inizio()) ||
                updatedLecture.getOraFine().isAfter(availability.getOra_fine())) {
            throw new RuntimeException("L'orario della lezione non rientra nella disponibilità del docente.");
        }

        // 6. Aggiorna i dati della lezione
        existing.setNomeLezione(updatedLecture.getNomeLezione());
        existing.setDocenteId(updatedLecture.getDocenteId());
        existing.setIdAula(updatedLecture.getIdAula());
        existing.setData(updatedLecture.getData());
        existing.setOraInizio(updatedLecture.getOraInizio());
        existing.setOraFine(updatedLecture.getOraFine());

        // 7. Salva su DB
        entityManager.merge(existing);
        return existing;
    }

    /**
     * Elimina una lezione dal sistema.
     *
     * @param id Identificatore della lezione da eliminare.
     */
    public void deleteLecture(String id) {
        Lecture lecture = getLectureById(id);
        if (lecture != null) {
            entityManager.remove(lecture);
        }
    }

    /**
     * Recupera tutte le lezioni presenti nel database.
     *
     * @return Lista di oggetti Lecture.
     */
    public List<Lecture> getAllLectures() {
        return entityManager.createQuery("SELECT l FROM Lecture l", Lecture.class).getResultList();
    }
}