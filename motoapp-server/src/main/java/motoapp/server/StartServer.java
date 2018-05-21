package motoapp.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import motoapp.persistence.CurseDBRepository;
import motoapp.persistence.EchipeDBRepository;
import motoapp.persistence.OperatoriDBRepository;
import motoapp.persistence.ParticipantiDBRepository;
import motoapp.services.CursaService;
import motoapp.services.EchipaService;
import motoapp.services.LoginService;
import motoapp.services.ParticipantService;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private Server server;

    private void start() throws IOException {
    /* The port on which the server should run */
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/motoappserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find motoappserver.properties " + e);
            return;
        }
        OperatoriDBRepository userRepository = new OperatoriDBRepository(serverProps);
        ParticipantiDBRepository participantiDBRepository = new ParticipantiDBRepository(serverProps);
        EchipeDBRepository echipeDBRepository = new EchipeDBRepository(serverProps);
        CurseDBRepository curseDBRepository = new CurseDBRepository(serverProps);

        LoginService loginService = new LoginService(userRepository);
        ParticipantService participantService = new ParticipantService(participantiDBRepository);
        EchipaService echipaService = new EchipaService(echipeDBRepository);
        CursaService cursaService = new CursaService(curseDBRepository);

        ServiceImpl serverImpl = new ServiceImpl(loginService, participantService, echipaService, cursaService);


        int port = 55555;
        server = ServerBuilder.forPort(port)
                .addService(serverImpl)
                .build()
                .start();
        //logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                StartServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */


    public static void main(String[] args) {
        final StartServer server = new StartServer();
        try {
            System.out.println("Starting server...");
            server.start();
            System.out.println("Server started!");
            server.blockUntilShutdown();
        } catch (Exception e) {
            System.err.println("Server exception:" + e);
            e.printStackTrace();
        }

    }
}
