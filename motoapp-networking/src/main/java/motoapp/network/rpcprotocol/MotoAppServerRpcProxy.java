package motoapp.network.rpcprotocol;

import motoapp.model.Cursa;
import motoapp.model.Echipa;
import motoapp.model.Operator;
import motoapp.model.Participant;
import motoapp.network.dto.*;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;
import motoapp.services.IMotoAppObserver;
import motoapp.services.IMotoAppServer;
import motoapp.services.MotoAppException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MotoAppServerRpcProxy implements IMotoAppServer {
    private String host;
    private int port;

    private IMotoAppObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public MotoAppServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingDeque<>();
    }

    @Override
    public void login(Operator operator, IMotoAppObserver client) throws MotoAppException {
        initializeConnection();
        UserDTO userDTO = DTOUtils.getDTO(operator);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new MotoAppException(err);
        }
    }

    @Override
    public void logout(Operator operator, IMotoAppObserver client) throws MotoAppException {
        UserDTO usrDTO = DTOUtils.getDTO(operator);
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(usrDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = null;
            finished = true;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new MotoAppException(err);
        }
    }

    @Override
    public void sendParticipant(Participant participant) throws MotoAppException {
        ParticipantDTO participantDTO = DTOUtils.getDTO(participant);
        Request request = new Request.Builder().type(RequestType.SEND_PARTICIPANT).data(participantDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
    }

    @Override
    public NrParticipanti[] getNrInscrisi() throws MotoAppException {
        Request request = new Request.Builder().type(RequestType.GET_NR_INSCRISI).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
        NrParticipantiDTO[] nrParticipantiDTO = (NrParticipantiDTO[]) response.data();
        NrParticipanti[] nrParticipanti = DTOUtils.getFromDTO(nrParticipantiDTO);
        return nrParticipanti;
    }

    @Override
    public String[] getEchipe() throws MotoAppException {
        Request request = new Request.Builder().type(RequestType.GET_NUME_ECHIPE).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
        String[] numeEchipe = (String[]) response.data();
        return numeEchipe;
    }

    @Override
    public String[] getCapacitati() throws MotoAppException {
        Request request = new Request.Builder().type(RequestType.GET_CAPACITATI).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
        String[] capacitati = (String[]) response.data();
        return capacitati;
    }

    @Override
    public ParticipantCursa[] getParticipantiCursaByEchipa(Echipa echipa) throws MotoAppException {
        EchipaDTO echipaDTO = DTOUtils.getDTO(echipa);
        Request request = new Request.Builder().type(RequestType.GET_PARTICIPANTI_CURSA).data(echipaDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
        ParticipantCursaDTO[] participantiCursaDTO = (ParticipantCursaDTO[]) response.data();
        ParticipantCursa[] participantiCursa = DTOUtils.getFromDTO(participantiCursaDTO);
        return participantiCursa;
    }

    @Override
    public Echipa getEchipaByName(String name) throws MotoAppException {
        Request request = new Request.Builder().type(RequestType.GET_ECHIPA_BY_NAME).data(name).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
        EchipaDTO echipaDTO = (EchipaDTO) response.data();
        Echipa echipa = DTOUtils.getFromDTO(echipaDTO);
        return echipa;
    }

    @Override
    public Cursa getCursaByCapacitate(int capacitate) throws MotoAppException {
        Integer capac = capacitate;
        Request request = new Request.Builder().type(RequestType.GET_CURSA_BY_CAPACITATE).data(capac).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new MotoAppException(err);
        }
        CursaDTO cursaDTO = (CursaDTO) response.data();
        Cursa cursa = DTOUtils.getFromDTO(cursaDTO);
        return cursa;
    }


    private void closeConnection() {
        finished = true;
        try {
            output.close();
            input.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) {
        try {
            System.out.println("Sending request... " + request.type());
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable {

        @Override
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        Thread tw = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    handleUpdate((Response)response);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        tw.start();

                    }else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }

            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.NEW_PARTICIPANT;
    }

    public void handleUpdate(Response response) throws RemoteException {
        if(response.type() == ResponseType.NEW_PARTICIPANT){
            try {
                client.addParticipant();
            } catch (MotoAppException e) {
                e.printStackTrace();
            }
        }
    }
}
