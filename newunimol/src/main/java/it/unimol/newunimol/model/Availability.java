package it.unimol.newunimol.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    private String idUtente; //PK

    private boolean disponibile;
    private String nome_utente;
    private String cognome_utente;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-25") //suggerisco a swagger di usare questo formato, quello di default non andava bene
    private LocalDate data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", format = "time", example = "09:00")
    private LocalTime ora_inizio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", format = "time", example = "13:00")
    private LocalTime ora_fine;

    // Getter e Setter
    public String getIdUtente() {
        return idUtente;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOra_inizio() {
        return ora_inizio;
    }

    public void setOra_inizio(LocalTime ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    public LocalTime getOra_fine() {
        return ora_fine;
    }

    public void setOra_fine(LocalTime ora_fine) {
        this.ora_fine = ora_fine;
    }





}