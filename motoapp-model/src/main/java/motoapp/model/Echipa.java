package motoapp.model;


public class Echipa implements HasId<Integer> {
    private int id;
    private String nume;

    public Echipa(int id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public Echipa(String nume) {
        this.nume = nume;
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
}
