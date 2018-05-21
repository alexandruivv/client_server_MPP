package motoapp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import motoapp.client.gui.LoginController;
import motoapp.client.gui.MainController;
import motoapp.services.IMotoAppServer;
import motoapp.network.rpcprotocol.MotoAppServerRpcProxy;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application{
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception{
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/motoappclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find motoappclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("motoapp.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("motoapp.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IMotoAppServer server = new MotoAppServerRpcProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("LoginView.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);


        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("MainView.fxml"));
        Parent croot=cloader.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(croot));

        MainController mainCtrl =
                cloader.<MainController>getController();
        mainCtrl.setServer(server);


        ctrl.setMainController(mainCtrl);
        ctrl.setStage(mainStage);

        primaryStage.setScene(new Scene(root));
        mainCtrl.setStage(primaryStage);
        primaryStage.show();

    }
}
