package motoapp.persistence.utils;

public class NrParticipanti {
    private int capacitate;
    private int nrInscrisi;

    public NrParticipanti(int capacitate, int nrInscrisi) {
        this.capacitate = capacitate;
        this.nrInscrisi = nrInscrisi;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }

    public int getNrInscrisi() {
        return nrInscrisi;
    }

    public void setNrInscrisi(int nrInscrisi) {
        this.nrInscrisi = nrInscrisi;
    }
}
