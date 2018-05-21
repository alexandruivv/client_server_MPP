package motoapp.services;

import motoapp.model.Participant;

public interface IMotoAppObserver {
    void addParticipant() throws MotoAppException;
}
