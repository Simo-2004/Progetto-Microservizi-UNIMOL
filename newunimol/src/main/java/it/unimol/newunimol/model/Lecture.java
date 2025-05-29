package it.unimol.newunimol.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    private String idLezione; // UUID o formato personalizzato

    private String nomeLezione;
    private String docenteId; // Riferimento all'ID del docente (es. idUtente da Availability)
    private String idAula; // Riferimento all'aula

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-25") //suggerisco a swagger di usare questo formato, quello di default non andava bene
    private LocalDate data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", format = "time", example = "09:00")
    private LocalTime oraInizio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", format = "time", example = "13:00")
    private LocalTime oraFine;

    // Getters and Setters
    public String getIdLezione() {
        return idLezione;
    }

    public void setIdLezione(String idLezione) {
        this.idLezione = idLezione;
    }

    public String getNomeLezione() {
        return nomeLezione;
    }

    public void setNomeLezione(String nomeLezione) {
        this.nomeLezione = nomeLezione;
    }

    public String getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(String docenteId) {
        this.docenteId = docenteId;
    }

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }
}