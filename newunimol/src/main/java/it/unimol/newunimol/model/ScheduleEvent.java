package it.unimol.newunimol.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleEvent {
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-05-25") //suggerisco a swagger di usare questo formato, quello di default non andava bene
    private LocalDate data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", format = "time", example = "09:00")
    private LocalTime oraInizio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", format = "time", example = "13:00")
    private LocalTime oraFine;

    private String idAula;
    private String idDocente;

    // Getters and Setters
}