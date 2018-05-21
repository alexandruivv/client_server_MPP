package motoapp.server;

import io.grpc.stub.StreamObserver;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;
import motoapp.services.CursaService;
import motoapp.services.EchipaService;
import motoapp.services.LoginService;
import motoapp.services.ParticipantService;

import java.util.ArrayList;

public class ServiceImpl extends IServiceGrpc.IServiceImplBase {
    private final LoginService loginService;
    private final ParticipantService participantService;
    private final EchipaService echipaService;
    private final CursaService cursaService;

    private ArrayList<StreamObserver<NrParticipantsGRPC>> clients = new ArrayList<>();

    public ServiceImpl(LoginService loginService, ParticipantService participantService, EchipaService echipaService,
                       CursaService cursaService) {
        this.loginService = loginService;
        this.participantService = participantService;
        this.echipaService = echipaService;
        this.cursaService = cursaService;
    }

    @Override
    public void login(motoapp.server.UserGRPC request,
                      io.grpc.stub.StreamObserver<motoapp.server.GRPCReply> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (loginService.login(username, password)) {
            responseObserver.onNext(GRPCReply.newBuilder().setStatus("OK").build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(GRPCReply.newBuilder().setStatus("FAILED").build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getNrParticipants(motoapp.server.RequestGRPC request,
                                  io.grpc.stub.StreamObserver<motoapp.server.NrParticipantsList> responseObserver) {
        NrParticipantsList.Builder participantsList = NrParticipantsList.newBuilder();
        for (NrParticipanti p : participantService.getNrParticipantsForEachCapacity()) {
            NrParticipantsGRPC.Builder participant = NrParticipantsGRPC.newBuilder();
            participant.setMotorCapacity(p.getCapacitate());
            participant.setNrParticipants(p.getNrInscrisi());
            participantsList.addNrParticipants(participant);
        }
        responseObserver.onNext(participantsList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getNumeEchipe(motoapp.server.RequestGRPC request,
                              io.grpc.stub.StreamObserver<motoapp.server.NumeEchipeList> responseObserver) {
        NumeEchipeList.Builder numeEchipeList = NumeEchipeList.newBuilder();
        for(String nume : echipaService.getNumeEchipe()){
            numeEchipeList.addNumeEchipa(nume);
        }
        responseObserver.onNext(numeEchipeList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCapacitati(motoapp.server.RequestGRPC request,
                              io.grpc.stub.StreamObserver<motoapp.server.CapacitatiList> responseObserver) {
        CapacitatiList.Builder capacitatiList = CapacitatiList.newBuilder();
        for(String capac : cursaService.getCapacitati()){
            capacitatiList.addCapacitati(capac);
        }
        responseObserver.onNext(capacitatiList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getParticipantiDupaEchipa(motoapp.server.NumeEchipaGRPC request,
                                          io.grpc.stub.StreamObserver<motoapp.server.ParticipantiCursaList> responseObserver) {
        String echipa = request.getNume();
        ParticipantiCursaList.Builder participantiList = ParticipantiCursaList.newBuilder();
        for (ParticipantCursa p : participantService.getParticipantiDupaEchipa(echipa)) {
            ParticipantCursaGRPC.Builder participant = ParticipantCursaGRPC.newBuilder();
            participant.setNume(p.getNume());
            participant.setCapacitate(p.getCapacitate());
            participantiList.addParticipanti(participant);
        }
        responseObserver.onNext(participantiList.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getEchipaDupaNume(motoapp.server.NumeEchipaGRPC request,
                                  io.grpc.stub.StreamObserver<motoapp.server.Echipa> responseObserver) {
        String nume = request.getNume();
        Echipa.Builder echipa = Echipa.newBuilder();
        motoapp.model.Echipa echFound = echipaService.getEchipaDupaNume(nume);
        echipa.setId(echFound.getId());
        echipa.setNume(echFound.getNume());
        responseObserver.onNext(echipa.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCursaDupaCapacitate(motoapp.server.CapacitateGRPC request,
                                       io.grpc.stub.StreamObserver<motoapp.server.Cursa> responseObserver) {
        String capac = request.getCapacitate();
        Cursa.Builder cursa = Cursa.newBuilder();
        motoapp.model.Cursa cFound = cursaService.getCursaDupaCapacitate(capac);
        cursa.setId(cFound.getId());
        cursa.setNume(cFound.getNume());
        cursa.setCapacitate(cFound.getCapacitate());
        responseObserver.onNext(cursa.build());
        responseObserver.onCompleted();
    }

    @Override
    public void saveParticipant(motoapp.server.Participant request,
                                io.grpc.stub.StreamObserver<motoapp.server.GRPCReply> responseObserver) {
        int id = request.getId();
        String nume = request.getNume();
        int idEchipa = request.getIdEchipa();
        int idCursa = request.getIdCursa();

        motoapp.model.Participant participant = new motoapp.model.Participant(id, nume, idEchipa, idCursa);
        participantService.save(participant);
        responseObserver.onNext(GRPCReply.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public io.grpc.stub.StreamObserver<motoapp.server.NrParticipantsGRPC> increment(
            io.grpc.stub.StreamObserver<motoapp.server.NrParticipantsGRPC> responseObserver) {
        clients.add(responseObserver);
        return new StreamObserver<motoapp.server.NrParticipantsGRPC>() {

            @Override
            public void onNext(motoapp.server.NrParticipantsGRPC value) {
                clients.forEach(client -> {
                    client.onNext(value);
                });
            }

            @Override
            public void onError(Throwable t) {
                clients.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                clients.remove(responseObserver);
            }
        };
    }

}
