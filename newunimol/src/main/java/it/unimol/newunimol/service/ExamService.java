package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Availability;
import it.unimol.newunimol.model.Exam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ExamService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private ConflictCheckerService conflictCheckerService; // <-- Servizio centralizzato per conflitti

    public Exam addExam(Exam newExam) {
        validateTeacherAvailability(newExam);
        checkRoomAvailability(newExam);
        entityManager.persist(newExam);
        return newExam;
    }

    public Exam getExamById(String id) {
        return entityManager.find(Exam.class, id);
    }

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

    public void deleteExam(String id) {
        Exam exam = getExamById(id);
        if (exam != null) {
            entityManager.remove(exam);
        }
    }

    public List<Exam> getAllExams() {
        return entityManager.createQuery("SELECT e FROM Exam e", Exam.class).getResultList();
    }

    // --- Validazioni aggiunte --- //

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