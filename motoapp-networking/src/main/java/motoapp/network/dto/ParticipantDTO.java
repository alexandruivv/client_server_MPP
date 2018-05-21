package motoapp.network.dto;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private int id;
    private String nume;
    private int idEchipa;
    private int idCursa;

    public ParticipantDTO(int id, String nume, int idEchipa, int idCursa) {
        this.id = id;
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
