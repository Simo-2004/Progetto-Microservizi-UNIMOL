package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Availability;
import it.unimol.newunimol.model.Exam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servizio per la gestione degli esami (Exam) nel sistema.
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
public class ExamService {

    /**
     * Gestore delle entità JPA usato per effettuare operazioni sul database.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Servizio per recuperare e verificare la disponibilità del docente.
     */
    @Autowired
    private AvailabilityService availabilityService;

    /**
     * Servizio centralizzato per verificare conflitti tra aule/docenti e nuovi impegni.
     */
    @Autowired
    private ConflictCheckerService conflictCheckerService; // <-- Servizio centralizzato per conflitti

    /**
     * Aggiunge un nuovo esame dopo aver effettuato tutte le verifiche necessarie:
     * - Disponibilità del docente,
     * - Disponibilità dell’aula,
     * - Assenza di conflitti con altri impegni.
     *
     * @param newExam L’oggetto Exam da inserire.
     * @return L’esame appena creato.
     */
    public Exam addExam(Exam newExam) {
        validateTeacherAvailability(newExam);
        checkRoomAvailability(newExam);
        entityManager.persist(newExam);
        return newExam;
    }

    /**
     * Recupera un esame in base al suo identificatore univoco.
     *
     * @param id Identificatore dell'esame.
     * @return L'oggetto Exam corrispondente, o null se non trovato.
     */
    public Exam getExamById(String id) {
        return entityManager.find(Exam.class, id);
    }

    /**
     * Aggiorna un esame esistente con i nuovi dati forniti,
     * dopo la verifica della disponibilità del docente e dell’aula.
     *
     * @param id Identificatore dell'esame da aggiornare.
     * @param updatedExam Oggetto contenente i nuovi dati.
     * @return L'esame aggiornato, o null se non esiste.
     */
    public Exam updateExam(String id, Exam updatedExam) {
        Exam existing = getExamById(id);
        if (existing == null) return null;

        validateTeacherAvailability(updatedExam);
        checkRoomAvailability(updatedExam);

        existing.setIdDocente(updatedExam.getIdDocente());
        existing.setIdAula(updatedExam.getIdAula());
        existing.setData(updatedExam.getData());
        existing.setOra_inizio(updatedExam.getOra_inizio());
        existing.setOra_fine(updatedExam.getOra_fine());
        existing.setTipo(updatedExam.getTipo());

        entityManager.merge(existing);
        return existing;
    }

    /**
     * Elimina un esame dal sistema.
     *
     * @param id Identificatore dell’esame da eliminare.
     */
    public void deleteExam(String id) {
        Exam exam = getExamById(id);
        if (exam != null) {
            entityManager.remove(exam);
        }
    }

    /**
     * Recupera tutti gli esami presenti nel database.
     *
     * @return Lista di oggetti Exam.
     */
    public List<Exam> getAllExams() {
        return entityManager.createQuery("SELECT e FROM Exam e", Exam.class).getResultList();
    }


    /**
     * Verifica che il docente sia disponibile per l’esame specificato.
     * Effettua controlli su:
     * - Esistenza della disponibilità,
     * - Stato di disponibilità attiva,
     * - Data e orario compatibili,
     * - Conflitti con altre attività.
     *
     * @param exam L’oggetto Exam da cui derivare i dati del controllo.
     */
    private void validateTeacherAvailability(Exam exam) {
        String idDocente = exam.getIdDocente();

        Availability availability = availabilityService.getAvailabilityById(idDocente)
                .orElseThrow(() -> new RuntimeException("Docente non trovato"));

        if (!availability.isDisponibile()) {
            throw new RuntimeException("Il docente non è disponibile.");
        }

        if (!availability.getData().isEqual(exam.getData())) {
            throw new RuntimeException("La data dell’esame non corrisponde alla disponibilità del docente.");
        }

        if (exam.getOra_inizio().isBefore(availability.getOra_inizio()) ||
                exam.getOra_fine().isAfter(availability.getOra_fine())) {
            throw new RuntimeException("L’orario dell’esame non rientra nella disponibilità del docente.");
        }

        // Controllo conflitti con altre lezioni/esami dello stesso docente
        if (!conflictCheckerService.isTeacherAvailable(
                idDocente,
                exam.getData(),
                exam.getOra_inizio(),
                exam.getOra_fine())) {
            throw new RuntimeException("Il docente ha un impegno in quell'orario.");
        }
    }

    /**
     * Verifica che l’aula sia disponibile nell’orario e nella data specificati.
     *
     * @param exam L’oggetto Exam da cui derivare i dati del controllo.
     */
    private void checkRoomAvailability(Exam exam) {
        if (!conflictCheckerService.isRoomAvailable(
                exam.getIdAula(),
                exam.getData(),
                exam.getOra_inizio(),
                exam.getOra_fine())) {
            throw new RuntimeException("L’aula non è disponibile in quell’orario.");
        }
    }
}