package motoapp.network.dto;

import motoapp.model.Cursa;
import motoapp.model.Echipa;
import motoapp.model.Operator;
import motoapp.model.Participant;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;

public class DTOUtils {
    public static UserDTO getDTO(Operator user) {
        String id = user.getUsername();
        String password = user.getPassword();
        return new UserDTO(id, password);
    }

    public static NrParticipantiDTO getDTO(NrParticipanti nrParticipanti) {
        int capacitate = nrParticipanti.getCapacitate();
        int nrInscrisi = nrParticipanti.getNrInscrisi();
        return new NrParticipantiDTO(capacitate, nrInscrisi);
    }

    public static NrParticipantiDTO[] getDTO(NrParticipanti[] nrParticipanti) {
        NrParticipantiDTO[] nrParticipantiDTOS = new NrParticipantiDTO[nrParticipanti.length];
        for (int i = 0; i < nrParticipanti.length; i++) {
            nrParticipantiDTOS[i] = getDTO(nrParticipanti[i]);
        }
        return nrParticipantiDTOS;
    }

    public static EchipaDTO getDTO(Echipa echipa) {
        int id = echipa.getId();
        String nume = echipa.getNume();
        return new EchipaDTO(id, nume);
    }

    public static EchipaDTO[] getDTO(Echipa[] echipa) {
        EchipaDTO[] echipaDTO = new EchipaDTO[echipa.length];
        for (int i = 0; i < echipa.length; i++) {
            echipaDTO[i] = getDTO(echipa[i]);
        }
        return echipaDTO;
    }

    public static ParticipantCursaDTO getDTO(ParticipantCursa participantCursa) {
        String nume = participantCursa.getNume();
        int capacitate = participantCursa.getCapacitate();
        return new ParticipantCursaDTO(nume, capacitate);
    }

    public static ParticipantCursaDTO[] getDTO(ParticipantCursa[] participantiCursa) {
        ParticipantCursaDTO[] participantiCursaDTO = new ParticipantCursaDTO[participantiCursa.length];
        for (int i = 0; i < participantiCursa.length; i++) {
            participantiCursaDTO[i] = getDTO(participantiCursa[i]);
        }
        return participantiCursaDTO;
    }

    public static CursaDTO getDTO(Cursa cursa){
        int id = cursa.getId();
        String nume = cursa.getNume();
        int capacitate = cursa.getCapacitate();
        return new CursaDTO(id, nume, capacitate);
    }

    public static ParticipantDTO getDTO(Participant participant){
        int id = participant.getId();
        String name = participant.getNume();
        int idEchipa = participant.getIdEchipa();
        int idCursa = participant.getIdCursa();
        return new ParticipantDTO(id, name, idEchipa, idCursa);
    }

    //-------------------------------------getFromDTO

    public static Operator getFromDTO(UserDTO usdto) {
        String id = usdto.getUsername();
        String pass = usdto.getPassword();
        return new Operator(id, pass);
    }

    public static NrParticipanti getFromDTO(NrParticipantiDTO nrParticipantiDTO) {
        int capacitate = nrParticipantiDTO.getCapacitate();
        int nrInscrisi = nrParticipantiDTO.getNrInscrisi();
        return new NrParticipanti(capacitate, nrInscrisi);
    }

    public static NrParticipanti[] getFromDTO(NrParticipantiDTO[] nrParticipantiDTO) {
        NrParticipanti[] nrParticipantis = new NrParticipanti[nrParticipantiDTO.length];
        for (int i = 0; i < nrParticipantiDTO.length; i++) {
            nrParticipantis[i] = getFromDTO(nrParticipantiDTO[i]);
        }
        return nrParticipantis;
    }

    public static Echipa getFromDTO(EchipaDTO echipa) {
        int id = echipa.getId();
        String nume = echipa.getNume();
        return new Echipa(id, nume);
    }

    public static Echipa[] getFromDTO(EchipaDTO[] echipa) {
        Echipa[] echipe = new Echipa[echipa.length];
        for (int i = 0; i < echipa.length; i++) {
            echipe[i] = getFromDTO(echipa[i]);
        }
        return echipe;
    }

    public static ParticipantCursa getFromDTO(ParticipantCursaDTO participantCursaDTO) {
        String nume = participantCursaDTO.getNume();
        int capacitate = participantCursaDTO.getCapacitate();
        return new ParticipantCursa(nume, capacitate);
    }

    public static ParticipantCursa[] getFromDTO(ParticipantCursaDTO[] participantiCursaDTO) {
        ParticipantCursa[] participantiCursa = new ParticipantCursa[participantiCursaDTO.length];
        for (int i = 0; i < participantiCursaDTO.length; i++) {
            participantiCursa[i] = getFromDTO(participantiCursaDTO[i]);
        }
        return participantiCursa;
    }

    public static Cursa getFromDTO(CursaDTO cursaDTO){
        int id = cursaDTO.getId();
        String nume = cursaDTO.getNume();
        int capacitate = cursaDTO.getCapacitate();
        return new Cursa(id, nume, capacitate);
    }

    public static Participant getFromDTO(ParticipantDTO participantDTO){
        int id = participantDTO.getId();
        String nume = participantDTO.getNume();
        int idEchipa = participantDTO.getIdEchipa();
        int idCursa =participantDTO.getIdCursa();
        return new Participant(id, nume, idEchipa, idCursa);
    }
}
