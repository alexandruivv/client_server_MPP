package motoapp.services;

import motoapp.model.Cursa;
import motoapp.model.Echipa;
import motoapp.model.Operator;
import motoapp.model.Participant;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMotoAppServer extends Remote{
    void login(Operator operator, IMotoAppObserver client) throws MotoAppException, RemoteException;
    void logout(Operator operator, IMotoAppObserver client) throws MotoAppException, RemoteException;
    void sendParticipant(Participant participant) throws MotoAppException, RemoteException;
    NrParticipanti[] getNrInscrisi() throws MotoAppException, RemoteException;
    String[] getEchipe() throws MotoAppException, RemoteException;
    String[] getCapacitati() throws MotoAppException, RemoteException;
    ParticipantCursa[] getParticipantiCursaByEchipa(Echipa echipa) throws MotoAppException, RemoteException;
    Echipa getEchipaByName(String name) throws MotoAppException, RemoteException;
    Cursa getCursaByCapacitate(int capacitate) throws MotoAppException, RemoteException;
}
