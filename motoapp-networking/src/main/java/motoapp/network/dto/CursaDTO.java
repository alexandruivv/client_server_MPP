package motoapp.network.dto;

import java.io.Serializable;

public class CursaDTO implements Serializable {
    private int id;
    private String nume;
    private int capacitate;

    public CursaDTO(int id, String nume, int capacitate) {
        this.id = id;
        this.nume = nume;
        this.capacitate = capacitate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
