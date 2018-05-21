package motoapp.model;


import java.io.Serializable;

public class Operator implements HasId<Integer>, Serializable {
    private int id;
    private String username;
    private String password;

    public Operator(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Operator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
