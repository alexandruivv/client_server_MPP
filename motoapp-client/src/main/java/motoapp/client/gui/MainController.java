package motoapp.client.gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import motoapp.model.Cursa;
import motoapp.model.Echipa;
import motoapp.model.Operator;
import motoapp.model.Participant;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;
import motoapp.services.IMotoAppObserver;
import motoapp.services.IMotoAppServer;
import motoapp.services.MotoAppException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class MainController implements IMotoAppObserver, Serializable {

    private Operator currentUser;
    private IMotoAppServer server;

    private Stage loginStage;

    @FXML
    Label usernameText;

    @FXML
    TableView<NrParticipanti> tableNrParticipanti;

    @FXML
    TableView<ParticipantCursa> tableEchipe;

    @FXML
    TableColumn<NrParticipanti, String> capacitateCol;

    @FXML
    TableColumn<NrParticipanti, String> inscrisiCol;

    @FXML
    TableColumn<ParticipantCursa, String> numePEchipaCol;

    @FXML
    TableColumn<ParticipantCursa, String> capacitateEchipaCol;
    @FXML
    ComboBox echipeBox;

    @FXML
    ComboBox capacitateIBox;

    @FXML
    ComboBox echipeIBox;

    @FXML
    TextField numeIText;

    public MainController(){
        try {
            UnicastRemoteObject.exportObject(this,0);
        } catch (RemoteException e) {
            System.out.println("Error exporting object "+e);
        }
    }

    void login(Operator operator) throws MotoAppException, RemoteException {
        server.login(operator, this);

    }

    @FXML
    public void onLogOut(MouseEvent mouseEvent) throws RemoteException {
        logout();
        loginStage.show();
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    void logout() throws RemoteException {
        try {
            server.logout(currentUser, this);
        } catch (MotoAppException e) {
            e.printStackTrace();
        }
    }

    public void setServer(IMotoAppServer server) {
        this.server = server;
    }

    public void setNrParticipanti() throws RemoteException {
        try {
            NrParticipanti[] nrParticipanti = server.getNrInscrisi();
            tableNrParticipanti.getItems().clear();
            for (NrParticipanti participant : nrParticipanti) {
                tableNrParticipanti.getItems().add(participant);
            }
        } catch (MotoAppException e) {
            e.printStackTrace();
        }
    }

    public void setDataComboBox() throws RemoteException {
        try {
            String[] echipe = server.getEchipe();
            echipeBox.getItems().setAll(echipe);
            echipeIBox.getItems().setAll(echipe);
            String[] capacitati = server.getCapacitati();
            capacitateIBox.getItems().setAll(capacitati);
        } catch (MotoAppException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.loginStage = stage;
    }

    public void setCurrentUser(Operator operator) {
        this.currentUser = operator;
        this.usernameText.setText(currentUser.getUsername());
    }

    @FXML
    public void initialize() {
        capacitateCol.setCellValueFactory(new PropertyValueFactory<NrParticipanti, String>("capacitate"));
        inscrisiCol.setCellValueFactory(new PropertyValueFactory<NrParticipanti, String>("nrInscrisi"));

        numePEchipaCol.setCellValueFactory(new PropertyValueFactory<ParticipantCursa, String>("nume"));
        capacitateEchipaCol.setCellValueFactory(new PropertyValueFactory<ParticipantCursa, String>("capacitate"));
    }

    @FXML
    public void onSelectionComboBoxChanged(ActionEvent ev) throws RemoteException {
        String numeEchipa = echipeBox.getSelectionModel().getSelectedItem().toString();
        Echipa echipa = new Echipa(-1, numeEchipa);
        try {
            ParticipantCursa[] participantCursas = server.getParticipantiCursaByEchipa(echipa);
            tableEchipe.getItems().clear();
            for (ParticipantCursa participant : participantCursas) {
                tableEchipe.getItems().add(participant);
            }
        } catch (MotoAppException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegisterParticipant(ActionEvent ev) throws RemoteException {
        String capacitateBox = capacitateIBox.getSelectionModel().getSelectedItem().toString();
        String echipaBox = echipeIBox.getSelectionModel().getSelectedItem().toString();

        String nume = numeIText.getText();
        int idEchipa = -1;
        try {
            Echipa echipa = server.getEchipaByName(echipaBox);
            idEchipa = echipa.getId();
        } catch (MotoAppException e) {
            e.printStackTrace();
        }

        int idCursa = -1;
        try {
            Cursa cursa = server.getCursaByCapacitate(Integer.parseInt(capacitateBox));
            idCursa = cursa.getId();
        } catch (MotoAppException e) {
            e.printStackTrace();
        }
        Participant newParticipant = new Participant(nume, idEchipa, idCursa);
        try {
            server.sendParticipant(newParticipant);
        } catch (MotoAppException e) {
            e.printStackTrace();
        }
        clearRegisterFields();
    }

    private void clearRegisterFields() {
        capacitateIBox.getSelectionModel().clearSelection();
        capacitateIBox.setPromptText("Neselectat");

        echipeIBox.getSelectionModel().clearSelection();
        echipeIBox.setPromptText("Neselectat");

        numeIText.clear();
    }

    @Override
    public void addParticipant() throws MotoAppException, RemoteException {
        setNrParticipanti();
    }
}
