package motoapp.persistence.utils;

public class ParticipantCursa {
    private String nume;
    private int capacitate;

    public ParticipantCursa(String nume, int capacitate) {
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
