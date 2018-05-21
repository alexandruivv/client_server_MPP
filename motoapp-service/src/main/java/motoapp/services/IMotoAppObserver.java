package motoapp.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMotoAppObserver extends Remote{
    void addParticipant() throws MotoAppException, RemoteException;
}
