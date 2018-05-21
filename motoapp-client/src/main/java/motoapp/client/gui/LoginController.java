package motoapp.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import motoapp.model.Operator;
import motoapp.services.IMotoAppServer;
import motoapp.services.MotoAppException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginController implements Serializable {

    private Operator crtUser;
    private IMotoAppServer server;

    private MainController mainCtrl;

    private Stage mainMotoAppStage;

    public LoginController(){
    }
    @FXML
    TextField usernameText;

    @FXML
    PasswordField passwordText;

    @FXML
    public void initialize(){
        usernameText.setText("iair2126");
        passwordText.setText("parola123");
    }
    @FXML
    public void on_btnClose_clicked() {
        System.exit(0);
    }

    @FXML
    public void on_btnLogIn(ActionEvent actionEvent) throws RemoteException {
        String username = usernameText.getText();
        String password = passwordText.getText();
        crtUser = new Operator(username, password);
        try {
            mainCtrl.login(crtUser);
            mainCtrl.setCurrentUser(crtUser);
            mainCtrl.setNrParticipanti();
            mainCtrl.setDataComboBox();
            mainMotoAppStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        mainCtrl.logout();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            });
            mainMotoAppStage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (MotoAppException|RemoteException e) {
            MessageAlert.showErrorMessage(e.getMessage());
        }
    }

    public void setServer(IMotoAppServer server){
        this.server = server;
    }

    public void setStage(Stage stage){
        this.mainMotoAppStage = stage;
    }

    public void setMainController(MainController ctrl){
        mainCtrl = ctrl;
    }
}
