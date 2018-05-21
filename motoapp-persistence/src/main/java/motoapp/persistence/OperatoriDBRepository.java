package motoapp.persistence;

import motoapp.model.Operator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OperatoriDBRepository{

    private JdbcUtils dbUtils;

    public OperatoriDBRepository(Properties props){
        dbUtils = new JdbcUtils(props);
    }

    /*@Override
    public int size() {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS [SIZE] FROM operatori")){
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    return result.getInt("SIZE");
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return 0;
    }

    @Override
    public void save(Operator entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO operatori (username, password) VALUES(?, ?)")){
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Operator entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE operatori SET username = ?, password = ? WHERE id = ?")){
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setInt(3, entity.getId());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void delete(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("DELETE FROM operatori WHERE id = ?")){
            preStmt.setInt(1, integer);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }*/

    public boolean operatorExists(Operator operator) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM operatori WHERE username = ? AND password = ?")){
            preStmt.setString(1, operator.getUsername());
            preStmt.setString(2, operator.getPassword());
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()) {
                    return true;
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    public Iterable<Operator> findAll() {
        Connection con = dbUtils.getConnection();
        List<Operator> curse = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM operatori")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("username");
                    String password = result.getString("password");
                    Operator operator = new Operator(id, nume, password);
                    curse.add(operator);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return curse;
    }
}
