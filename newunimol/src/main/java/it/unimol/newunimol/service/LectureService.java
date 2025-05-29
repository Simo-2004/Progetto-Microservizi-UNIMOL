package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Lecture;
import it.unimol.newunimol.model.Availability;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LectureService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AvailabilityService availabilityService; // Servizio per recuperare la disponibilità

    public Lecture addLecture(Lecture newLecture) {
        String docenteId = newLecture.getDocenteId();

        // Recupera la disponibilità del docente
        Availability availability = availabilityService.getAvailabilityById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente non trovato"));

        // Verifica se è disponibile
        if (!availability.isDisponibile()) {
            throw new RuntimeException("Il docente non è disponibile.");
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

    public Lecture getLectureById(String id) {
        return entityManager.find(Lecture.class, id);
    }

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

    public void deleteLecture(String id) {
        Lecture lecture = getLectureById(id);
        if (lecture != null) {
            entityManager.remove(lecture);
        }
    }

    public List<Lecture> getAllLectures() {
        return entityManager.createQuery("SELECT l FROM Lecture l", Lecture.class).getResultList();
    }
}