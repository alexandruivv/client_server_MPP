package motoapp.model;

public class Participant implements HasId<Integer> {
    private int id;
    private String nume;
    private int idEchipa;
    private int idCursa;

    public Participant(int id, String nume, int idEchipa, int idCursa) {
        this.id = id;
        this.nume = nume;
        this.idEchipa = idEchipa;
        this.idCursa = idCursa;
    }

    public Participant(String nume, int idEchipa, int idCursa) {
        this.nume = nume;
        this.idEchipa = idEchipa;
        this.idCursa = idCursa;
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

    public int getIdEchipa() {
        return idEchipa;
    }

    public void setIdEchipa(int idEchipa) {
        this.idEchipa = idEchipa;
    }

    public int getIdCursa() {
        return idCursa;
    }

    public void setIdCursa(int idCursa) {
        this.idCursa = idCursa;
    }
}
