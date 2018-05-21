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

import java.io.IOException;

public class LoginController {

    private Operator crtUser;
    private IMotoAppServer server;

    private MainController mainCtrl;

    Stage mainMotoAppStage;

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
    public void on_btnLogIn(ActionEvent actionEvent){
        String username = usernameText.getText();
        String password = passwordText.getText();
        crtUser = new Operator(username, password);
        try {
            server.login(crtUser, mainCtrl);
            mainCtrl.setCurrentUser(crtUser);
            mainCtrl.setNrParticipanti();
            mainCtrl.setDataComboBox();
            mainMotoAppStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainCtrl.logout();
                    System.exit(0);
                }
            });
            mainMotoAppStage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch (MotoAppException e) {
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
