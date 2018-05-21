package motoapp.network.dto;

import java.io.Serializable;

public class ParticipantCursaDTO implements Serializable{
    private String nume;
    private int capacitate;

    public ParticipantCursaDTO(String nume, int capacitate) {
        this.nume = nume;
        this.capacitate = capacitate;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }
}
