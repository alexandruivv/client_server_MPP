package motoapp.persistence;


import motoapp.model.Cursa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CurseDBRepository implements IRepository<Cursa, Integer>{

    private JdbcUtils dbUtils;

    public CurseDBRepository(Properties props){
        this.dbUtils = new JdbcUtils(props);
    }


    @Override
    public int size() {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS [SIZE] FROM curse")){
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    return result.getInt("SIZE");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(Cursa entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO curse (nume, capacitate) VALUES(?, ?)")){
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getCapacitate());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Cursa entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE curse SET nume = ?, capacitate = ? WHERE id = ?")){
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getCapacitate());
            preStmt.setInt(3, entity.getId());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void delete(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("DELETE FROM curse WHERE id = ?")){
            preStmt.setInt(1, integer);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Cursa getOne(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM curse WHERE id = ?")){
            preStmt.setInt(1, integer);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()) {
                    String nume = result.getString("nume");
                    int capacitate = result.getInt("capacitate");
                    Cursa cursa = new Cursa(integer, nume, capacitate);
                    return cursa;
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public Iterable<Cursa> findAll() {
        Connection con = dbUtils.getConnection();
        List<Cursa> curse = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM curse")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    int capacitate = result.getInt("capacitate");
                    Cursa cursa = new Cursa(id, nume, capacitate);
                    curse.add(cursa);
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return curse;
    }

    public List<String> getCapacitati(){
        Connection con = dbUtils.getConnection();
        List<String> list = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT DISTINCT capacitate FROM curse")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    String nume = result.getString(1);
                    list.add(nume);
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return list;
    }

    public Cursa getCursaByCapacitate(int capacitate){
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM curse WHERE capacitate = ?")){
            preStmt.setInt(1, capacitate);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    int capac = result.getInt("capacitate");
                    return new Cursa(id, nume, capac);
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return null;
    }
}
