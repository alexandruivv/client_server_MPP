package motoapp.client.gui;

import javafx.scene.control.Alert;

public class MessageAlert {
    public static void showErrorMessage(String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showInfoMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
