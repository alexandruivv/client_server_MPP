package motoapp.network.rpcprotocol;

import com.sun.org.apache.regexp.internal.RE;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class MotoAppClientReflectionWorker implements Runnable, IMotoAppObserver {
    private IMotoAppServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public MotoAppClientReflectionWorker(IMotoAppServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleRequest(Request request) {
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }
    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        UserDTO udto=(UserDTO)request.data();
        Operator user= DTOUtils.getFromDTO(udto);
        try {
            server.login(user, this);
            return okResponse;
        } catch (MotoAppException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("Logout request... " + request.type());
        UserDTO udto = (UserDTO)request.data();
        Operator operator = DTOUtils.getFromDTO(udto);
        try{
            server.logout(operator, this);
            connected = false;
            return okResponse;

        }catch(MotoAppException e){
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_NR_INSCRISI(Request request){
        System.out.println("Nr inscrisi request ..." + request.type());
        try{
            NrParticipanti[] nrParticipanti = server.getNrInscrisi();
            NrParticipantiDTO[] nrParticipantiDTO = DTOUtils.getDTO(nrParticipanti);
            return new Response.Builder().type(ResponseType.GET_NR_INSCRISI).data(nrParticipantiDTO).build();
        }catch (MotoAppException ex){
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleGET_NUME_ECHIPE(Request request){
        System.out.println("Nume echipe request ..." + request.type());
        try{
            String[] echipe = server.getEchipe();
            return new Response.Builder().type(ResponseType.GET_NUME_ECHIPE).data(echipe).build();
        }catch(MotoAppException e){
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CAPACITATI(Request request){
        System.out.println("Capacitati request... " + request.type());
        try {
            String[] capacitati = server.getCapacitati();
            return new Response.Builder().type(ResponseType.GET_CAPACITATI).data(capacitati).build();
        } catch (MotoAppException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_PARTICIPANTI_CURSA(Request request){
        System.out.println("Participanti cursa request ... " + request.type());
        EchipaDTO echipaDTO = (EchipaDTO)request.data();
        Echipa echipa = DTOUtils.getFromDTO(echipaDTO);
        try {
            ParticipantCursa[] participantiCursa = server.getParticipantiCursaByEchipa(echipa);
            ParticipantCursaDTO[] participantiCursaDTO = DTOUtils.getDTO(participantiCursa);
            return new Response.Builder().type(ResponseType.GET_PARTICIPANTI_CURSA).data(participantiCursaDTO).build();
        } catch (MotoAppException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ECHIPA_BY_NAME(Request request){
        System.out.println("Echipa by name request ..." + request.type());
        try {
            Echipa echipa = server.getEchipaByName((String)request.data());
            EchipaDTO echipaDTO = DTOUtils.getDTO(echipa);
            return new Response.Builder().type(ResponseType.GET_ECHIPA_BY_NAME).data(echipaDTO).build();
        } catch (MotoAppException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CURSA_BY_CAPACITATE(Request request){
        System.out.println("Cursa by capacitate request ..." + request.type());
        try {
            Cursa cursa = server.getCursaByCapacitate((Integer)request.data());
            CursaDTO cursaDTO = DTOUtils.getDTO(cursa);
            return new Response.Builder().type(ResponseType.GET_CURSA_BY_CAPACITATE).data(cursaDTO).build();
        } catch (MotoAppException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSEND_PARTICIPANT(Request request){
        System.out.println("Add new participant request..." + request.type());
        ParticipantDTO participantDTO = (ParticipantDTO)request.data();
        Participant participant = DTOUtils.getFromDTO(participantDTO);
        try {
            server.sendParticipant(participant);
            return okResponse;
        } catch (MotoAppException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    @Override
    public void addParticipant() throws MotoAppException {
        Response response = new Response.Builder().type(ResponseType.NEW_PARTICIPANT).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
