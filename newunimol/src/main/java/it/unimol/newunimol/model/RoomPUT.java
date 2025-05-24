package it.unimol.newunimol.model;

public class RoomPUT {


    private String nome;
    private String edificio;
    private int capienza;
    private boolean disponibile;
    private String dotazioni; //per esempio Computer, Sedie ecc...

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
