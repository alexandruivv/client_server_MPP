package motoapp.server;

import motoapp.network.utils.AbstractServer;
import motoapp.network.utils.MotoAppRpcConcurrentServer;
import motoapp.network.utils.ServerException;
import motoapp.persistence.CurseDBRepository;
import motoapp.persistence.EchipeDBRepository;
import motoapp.persistence.OperatoriDBRepository;
import motoapp.persistence.ParticipantiDBRepository;
import motoapp.services.IMotoAppServer;

import java.io.IOException;

import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/motoappserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find motoappserver.properties " + e);
            return;
        }
        OperatoriDBRepository userRepo = new OperatoriDBRepository(serverProps);
        ParticipantiDBRepository participantiRepo = new ParticipantiDBRepository(serverProps);
        EchipeDBRepository echipeDBRepository = new EchipeDBRepository(serverProps);
        CurseDBRepository curseDBRepository = new CurseDBRepository(serverProps);
        IMotoAppServer motoAppServerImp = new MotoAppServerImpl(userRepo, participantiRepo, echipeDBRepository,
                curseDBRepository);
        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("motoapp.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new MotoAppRpcConcurrentServer(chatServerPort, motoAppServerImp);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
