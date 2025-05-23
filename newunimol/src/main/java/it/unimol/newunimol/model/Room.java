package it.unimol.newunimol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    private String idAula; //PK

    private String nome;
    private String edificio;
    private int capienza;
    private boolean disponibile;
    private String dotazioni; //per esempio Computer, Sedie ecc...

    //Getters e Setters
    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public String getDotazioni() {
        return dotazioni;
    }

    public void setDotazioni(String dotazioni) {
        this.dotazioni = dotazioni;
    }
}
