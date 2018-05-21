package motoapp.model;


public class Cursa implements HasId<Integer> {
    private int id;
    private String nume;
    private int capacitate;

    public Cursa(int id, String nume, int capacitate) {
        this.id = id;
        this.nume = nume;
        this.capacitate = capacitate;
    }

    public Cursa(String nume, int capacitate) {
        this.nume = nume;
        this.capacitate = capacitate;
    }

    public Integer getId() {
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

    @Override
    public String toString() {
        return "Cursa{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", capacitate=" + capacitate +
                '}';
    }
}
