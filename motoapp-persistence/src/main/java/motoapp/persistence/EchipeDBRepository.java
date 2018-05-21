package motoapp.persistence;


import motoapp.model.Echipa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EchipeDBRepository implements IRepository<Echipa, Integer> {
    private JdbcUtils dbUtils;

    public EchipeDBRepository(Properties props){
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public int size() {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS [SIZE] FROM echipe")){
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
    public void save(Echipa entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO echipe (nume) VALUES(?)")){
            preStmt.setString(1, entity.getNume());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Echipa entity){
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE echipe SET nume = ? WHERE id = ?")){
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getId());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void delete(Integer integer){
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("DELETE FROM echipe WHERE id = ?")){
            preStmt.setInt(1, integer);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Echipa getOne(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM echipe WHERE id = ?")){
            preStmt.setInt(1, integer);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()) {
                    String nume = result.getString("nume");
                    Echipa echipa = new Echipa(integer, nume);
                    return echipa;
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public Iterable<Echipa> findAll() {
        Connection con = dbUtils.getConnection();
        List<Echipa> curse = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM echipe")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    Echipa echipa = new Echipa(id, nume);
                    curse.add(echipa);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return curse;
    }

    public List<String> getNumeEchipe(){
        Connection con = dbUtils.getConnection();
        List<String> list = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT DISTINCT nume FROM echipe")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String nume = result.getString("nume");
                    list.add(nume);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return list;
    }

    public Echipa getEchipaByName(String nume){
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM echipe WHERE nume = ?")) {
            preStmt.setString(1, nume);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("nume");
                    return new Echipa(id, name);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return null;
    }
}
