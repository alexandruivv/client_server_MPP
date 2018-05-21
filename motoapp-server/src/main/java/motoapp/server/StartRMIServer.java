package motoapp.server;

import motoapp.persistence.CurseDBRepository;
import motoapp.persistence.EchipeDBRepository;
import motoapp.persistence.OperatoriDBRepository;
import motoapp.persistence.ParticipantiDBRepository;
import motoapp.services.IMotoAppServer;


import java.io.IOException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class StartRMIServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRMIServer.class.getResourceAsStream("/motoappserver.properties"));
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
        System.setProperty("java.security.policy","file:./MotoApp.policy");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try{

            String name = serverProps.getProperty("motoapp.rmi.serverID", "Moto");
            IMotoAppServer stub = (IMotoAppServer) UnicastRemoteObject.exportObject(motoAppServerImp, 0);
            Registry registry = LocateRegistry.getRegistry();
            System.out.println("before binding");
            registry.rebind(name, stub);
            System.out.println("Chat server   bound");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
