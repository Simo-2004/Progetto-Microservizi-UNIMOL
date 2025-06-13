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

/**
 * Servizio per verificare conflitti di disponibilità tra aule e docenti.
 * Fornisce metodi interni per controllare se un'aula o un docente sono già impegnati
 * in un determinato orario e data, confrontando con lezioni ed esami esistenti.
 */

@Service
@Transactional
public class ConflictCheckerService {

    /**
     * Gestore delle entità JPA usato per effettuare query e operazioni sul database.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Verifica se l'aula specificata è disponibile nell'orario e nella data forniti.
     * Controlla sia le lezioni che gli esami previsti per quell'aula nello stesso giorno,
     * e verifica anche lo stato di disponibilità dell'aula stessa.
     *
     * @param idAula Identificatore univoco dell'aula da verificare.
     * @param data Data in cui si desidera effettuare il controllo.
     * @param start Ora di inizio del nuovo impegno.
     * @param end Ora di fine del nuovo impegno.
     * @return
     * true se l'aula è disponibile.
     * false altrimenti.
     */
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

    /**
     * Verifica se il docente è disponibile nell'orario e nella data specificati.
     * Controlla eventuali sovrapposizioni con lezioni o esami a cui il docente partecipa.
     *
     * @param idDocente Identificatore univoco del docente.
     * @param data Data in cui si desidera effettuare il controllo.
     * @param start Ora di inizio del nuovo impegno.
     * @param end Ora di fine del nuovo impegno.
     * @return
     * true se il docente è disponibile.
     * false altrimenti.
     */
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

    /**
     * Metodo ausiliario per verificare la sovrapposizione tra due intervalli temporali.
     *
     * @param newStart Ora di inizio del nuovo impegno.
     * @param newEnd Ora di fine del nuovo impegno.
     * @param existingStart Ora di inizio dell'impegno esistente.
     * @param existingEnd Ora di fine dell'impegno esistente.
     * @return
     * true se gli intervalli si sovrappongono.
     * false altrimenti.
     */
    private boolean isOverlapping(LocalTime newStart, LocalTime newEnd,
                                  LocalTime existingStart, LocalTime existingEnd) {
        return !newEnd.isBefore(existingStart) && !newStart.isAfter(existingEnd);
    }

    /**
     * Recupera tutte le lezioni tenute in una certa aula in una specifica data.
     *
     * @param idAula Identificatore dell'aula.
     * @param data Data in cui cercare le lezioni.
     *
     */
    private List<Lecture> getLecturesByRoomAndDate(String idAula, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT l FROM Lecture l WHERE l.idAula = :idAula AND l.data = :data", Lecture.class)
                .setParameter("idAula", idAula)
                .setParameter("data", data)
                .getResultList();
    }

    /**
     * Recupera tutti gli esami svolti in una certa aula in una specifica data.
     *
     * @param idAula Identificatore dell'aula.
     * @param data Data in cui cercare gli esami.
     */
    private List<Exam> getExamsByRoomAndDate(String idAula, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT e FROM Exam e WHERE e.idAula = :idAula AND e.data = :data", Exam.class)
                .setParameter("idAula", idAula)
                .setParameter("data", data)
                .getResultList();
    }

    /**
     * Recupera tutte le lezioni tenute da un certo docente in una specifica data.
     *
     * @param idDocente Identificatore del docente.
     * @param data Data in cui cercare le lezioni.
     */
    private List<Lecture> getLecturesByTeacherAndDate(String idDocente, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT l FROM Lecture l WHERE l.docenteId = :idDocente AND l.data = :data", Lecture.class)
                .setParameter("idDocente", idDocente)
                .setParameter("data", data)
                .getResultList();
    }

    /**
     * Recupera tutti gli esami in cui è coinvolto un certo docente in una specifica data.
     *
     * @param idDocente Identificatore del docente.
     * @param data Data in cui cercare gli esami.
     */
    private List<Exam> getExamsByTeacherAndDate(String idDocente, LocalDate data) {
        return entityManager.createQuery(
                        "SELECT e FROM Exam e WHERE e.idDocente = :idDocente AND e.data = :data", Exam.class)
                .setParameter("idDocente", idDocente)
                .setParameter("data", data)
                .getResultList();
    }
}