package it.unimol.newunimol.service;

import it.unimol.newunimol.model.Exam;
import it.unimol.newunimol.model.Lecture;
import it.unimol.newunimol.model.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ConflictCheckerService {

    @PersistenceContext
    private EntityManager entityManager;

    // Verifica se l'aula è disponibile
    public boolean isRoomAvailable(String idAula, LocalDate data, LocalTime start, LocalTime end) {
        List<Lecture> lectures = getLecturesByRoomAndDate(idAula, data);
        List<Exam> exams = getExamsByRoomAndDate(idAula, data);

        Room room = entityManager.find(Room.class, idAula);


        if (room == null) {
            throw new RuntimeException("Aula non trovata.");
        }


        if (!room.isDisponibile()) {
            return false;
        }


        for (Lecture lecture : lectures) {
            if (isOverlapping(start, end, lecture.getOraInizio(), lecture.getOraFine())) {
                return false;
            }
        }

        for (Exam exam : exams) {
            if (isOverlapping(start, end, exam.getOra_inizio(), exam.getOra_fine())) {
                return false;
            }
        }

        return true;
    }

    // Verifica se il docente ha impegni nello stesso orario
    public boolean isTeacherAvailable(String idDocente, LocalDate data, LocalTime start, LocalTime end) {
        List<Lecture> lectures = getLecturesByTeacherAndDate(idDocente, data);
        List<Exam> exams = getExamsByTeacherAndDate(idDocente, data);

        for (Lecture lecture : lectures) {
            if (isOverlapping(start, end, lecture.getOraInizio(), lecture.getOraFine())) {
                return false;
            }
        }

        for (Exam exam : exams) {
            if (isOverlapping(start, end, exam.getOra_inizio(), exam.getOra_fine())) {
                return false;
            }
        }

        return true;
    }

    // Metodo di utilità per verificare sovrapposizioni
    private boolean isOverlapping(LocalTime newStart, LocalTime newEnd,
                                  LocalTime existingStart, LocalTime existingEnd) {
        return !newEnd.isBefore(existingStart) && !newStart.isAfter(existingEnd);
    }

    // Query JPA per recuperare dati
    private List<Lecture> getLecturesByRoomAndDate(String idAula, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT l FROM Lecture l WHERE l.idAula = :idAula AND l.data = :data", Lecture.class)
                .setParameter("idAula", idAula)
                .setParameter("data", data)
                .getResultList();
    }

    private List<Exam> getExamsByRoomAndDate(String idAula, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT e FROM Exam e WHERE e.idAula = :idAula AND e.data = :data", Exam.class)
                .setParameter("idAula", idAula)
                .setParameter("data", data)
                .getResultList();
    }

    private List<Lecture> getLecturesByTeacherAndDate(String idDocente, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT l FROM Lecture l WHERE l.docenteId = :idDocente AND l.data = :data", Lecture.class)
                .setParameter("idDocente", idDocente)
                .setParameter("data", data)
                .getResultList();
    }

    private List<Exam> getExamsByTeacherAndDate(String idDocente, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT e FROM Exam e WHERE e.idDocente = :idDocente AND e.data = :data", Exam.class)
                .setParameter("idDocente", idDocente)
                .setParameter("data", data)
                .getResultList();
    }
}