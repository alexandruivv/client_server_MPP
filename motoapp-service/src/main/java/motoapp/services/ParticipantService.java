package motoapp.services;

import motoapp.model.Participant;
import motoapp.persistence.ParticipantiDBRepository;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;

import java.util.List;

public class ParticipantService {
    private ParticipantiDBRepository repository;

    public ParticipantService(ParticipantiDBRepository repository) {
        this.repository = repository;
    }

    public void save(Participant p){
        repository.save(p);
    }

    public List<NrParticipanti> getNrParticipantsForEachCapacity(){
        return repository.getNrParticipantiByCapacitate();
    }

    public List<ParticipantCursa> getParticipantiDupaEchipa(String echipa){
        return repository.getParticipantiWithCapacitateByEchipa(echipa);
    }
}
