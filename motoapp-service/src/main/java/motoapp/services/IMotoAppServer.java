package motoapp.services;

import motoapp.model.Cursa;
import motoapp.model.Echipa;
import motoapp.model.Operator;
import motoapp.model.Participant;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;

public interface IMotoAppServer {
    void login(Operator operator, IMotoAppObserver client) throws MotoAppException;
    void logout(Operator operator, IMotoAppObserver client) throws MotoAppException;
    void sendParticipant(Participant participant) throws MotoAppException;
    NrParticipanti[] getNrInscrisi() throws MotoAppException;
    String[] getEchipe() throws MotoAppException;
    String[] getCapacitati() throws MotoAppException;
    ParticipantCursa[] getParticipantiCursaByEchipa(Echipa echipa) throws MotoAppException;
    Echipa getEchipaByName(String name) throws MotoAppException;
    Cursa getCursaByCapacitate(int capacitate) throws MotoAppException;
}
