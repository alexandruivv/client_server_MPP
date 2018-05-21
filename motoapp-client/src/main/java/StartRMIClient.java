import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import motoapp.client.gui.LoginController;
import motoapp.client.gui.MainController;
import motoapp.services.IMotoAppServer;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class StartRMIClient extends Application{
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception{
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRMIClient.class.getResourceAsStream("/motoappclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find motoappclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("motoapp.server.host", defaultServer);
        String name = clientProps.getProperty("motoapp.rmi.serverID");

        try {

            Registry registry = LocateRegistry.getRegistry(serverIP);
            IMotoAppServer server = (IMotoAppServer) registry.lookup(name);
            System.out.println("Obtained a reference to remote chat server");
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

        } catch (Exception e) {
            System.err.println("Chat Initialization  exception:"+e);
            e.printStackTrace();
        }





    }
}
