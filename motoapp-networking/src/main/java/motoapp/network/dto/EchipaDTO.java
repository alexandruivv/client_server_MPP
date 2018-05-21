package motoapp.network.dto;

import java.io.Serializable;

public class EchipaDTO implements Serializable{
    private int id;
    private String nume;

    public EchipaDTO(int id, String nume) {
        this.id = id;
        this.nume = nume;
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
}
