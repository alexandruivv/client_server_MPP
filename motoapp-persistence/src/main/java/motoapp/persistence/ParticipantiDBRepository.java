package motoapp.persistence;

import motoapp.model.Participant;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantiDBRepository implements IRepository<Participant, Integer> {

    public JdbcUtils dbUtils;

    public ParticipantiDBRepository(Properties props){
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public int size() {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS [SIZE] FROM participanti")){
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
    public void save(Participant entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("INSERT INTO participanti (nume, idEchipa, idCursa) VALUES(?, ?, ?)")){
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getIdEchipa());
            preStmt.setInt(3, entity.getIdCursa());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Participant entity) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("UPDATE participanti SET nume = ?, idEchipa = ?, idCursa = ? WHERE id = ?")){
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getIdEchipa());
            preStmt.setInt(3, entity.getIdCursa());
            preStmt.setInt(4, entity.getId());
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void delete(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("DELETE FROM participanti WHERE id = ?")){
            preStmt.setInt(1, integer);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Participant getOne(Integer integer) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("SELECT * FROM participanti WHERE id = ?")){
            preStmt.setInt(1, integer);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    int idEchipa = result.getInt("idEchipa");
                    int idCursa = result.getInt("idCursa");
                    Participant participant = new Participant(id, nume, idEchipa, idCursa);
                    return participant;
                }
            }
        }catch (SQLException ex){
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public Iterable<Participant> findAll() {
        Connection con = dbUtils.getConnection();
        List<Participant> curse = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM participanti")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    int idEchipa = result.getInt("idEchipa");
                    int idCursa = result.getInt("idCursa");
                    Participant participant = new Participant(id, nume, idEchipa, idCursa);
                    curse.add(participant);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return curse;
    }

    public List<NrParticipanti> getNrParticipantiByCapacitate(){
        Connection con = dbUtils.getConnection();
        List<NrParticipanti> list = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT C.capacitate, COUNT(DISTINCT P.id) AS nrinscrisi " +
                "FROM participanti AS P " +
                "INNER JOIN curse AS C ON C.id = P.idCursa " +
                "GROUP BY C.capacitate")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int capacitate = result.getInt(1);
                    int nr = result.getInt(2);
                    NrParticipanti nrParticipanti = new NrParticipanti(capacitate, nr);
                    list.add(nrParticipanti);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return list;
    }

    public List<ParticipantCursa> getParticipantiWithCapacitateByEchipa(String echipa){
        Connection con = dbUtils.getConnection();
        List<ParticipantCursa> list = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT P.nume, C.capacitate\n" +
                "FROM participanti P\n" +
                "INNER JOIN echipe E ON E.id = P.idEchipa\n" +
                "INNER JOIN curse C ON C.id = P.idCursa\n" +
                "WHERE E.nume = ?")) {
            preStmt.setString(1, echipa);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String nume = result.getString(1);
                    int capacitate = result.getInt(2);
                    ParticipantCursa participantCursa = new ParticipantCursa(nume, capacitate);
                    list.add(participantCursa);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return list;
    }
}
