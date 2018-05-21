package motoapp.server;

import motoapp.model.Cursa;
import motoapp.model.Echipa;
import motoapp.model.Operator;
import motoapp.model.Participant;
import motoapp.persistence.CurseDBRepository;
import motoapp.persistence.EchipeDBRepository;
import motoapp.persistence.OperatoriDBRepository;
import motoapp.persistence.ParticipantiDBRepository;
import motoapp.persistence.utils.NrParticipanti;
import motoapp.persistence.utils.ParticipantCursa;
import motoapp.services.IMotoAppObserver;
import motoapp.services.IMotoAppServer;
import motoapp.services.MotoAppException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MotoAppServerImpl implements IMotoAppServer {

    private OperatoriDBRepository userRepository;
    private ParticipantiDBRepository participantiRepository;
    private EchipeDBRepository echipeDBRepository;
    private CurseDBRepository curseDBRepository;
    private Map<String, IMotoAppObserver> loggedClients;

    public MotoAppServerImpl(OperatoriDBRepository uRepo, ParticipantiDBRepository pRepo, EchipeDBRepository eRepo,
                             CurseDBRepository cRepo) {
        userRepository = uRepo;
        participantiRepository = pRepo;
        echipeDBRepository = eRepo;
        curseDBRepository = cRepo;
        loggedClients = new ConcurrentHashMap<>();
        ;
    }


    public synchronized void login(Operator user, IMotoAppObserver client) throws MotoAppException {
        boolean userExists = userRepository.operatorExists(user);
        if (userExists) {
            if (loggedClients.get(user.getUsername()) != null)
                throw new MotoAppException("Utilizatorul este deja logat !");
            loggedClients.put(user.getUsername(), client);
        } else
            throw new MotoAppException("Date invalide !");
    }

    @Override
    public void logout(Operator operator, IMotoAppObserver client) throws MotoAppException {
        IMotoAppObserver localClient = loggedClients.remove(operator.getUsername());
        if (localClient == null) {
            throw new MotoAppException("Utilizatorul " + operator.getUsername() + " nu este logat !");
        }
    }

    private final int defaultThreadsNo = 5;

    @Override
    public void sendParticipant(Participant participant) throws MotoAppException {
        participantiRepository.save(participant);
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

        List<Operator> users = (List<Operator>) userRepository.findAll();
        for (Operator user : users) {
            IMotoAppObserver motoClient = loggedClients.get(user.getUsername());
            if (motoClient != null) {
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying client");
                        motoClient.addParticipant();
                    } catch (MotoAppException e) {
                        System.err.println("Error notifying client " + e);
                    }
                });
            }
        }
    }



    @Override
    public NrParticipanti[] getNrInscrisi() throws MotoAppException {
        List<NrParticipanti> nrParticipanti = participantiRepository.getNrParticipantiByCapacitate();

        return nrParticipanti.toArray(new NrParticipanti[nrParticipanti.size()]);
    }

    @Override
    public String[] getEchipe() throws MotoAppException {
        List<String> echipe = echipeDBRepository.getNumeEchipe();
        return echipe.toArray(new String[echipe.size()]);
    }

    @Override
    public String[] getCapacitati() throws MotoAppException {
        List<String> capacitati = curseDBRepository.getCapacitati();
        return capacitati.toArray(new String[capacitati.size()]);
    }

    @Override
    public ParticipantCursa[] getParticipantiCursaByEchipa(Echipa echipa) throws MotoAppException {
        List<ParticipantCursa> participantiCursa = participantiRepository.getParticipantiWithCapacitateByEchipa(echipa.getNume());
        return participantiCursa.toArray(new ParticipantCursa[participantiCursa.size()]);
    }

    @Override
    public Echipa getEchipaByName(String name) throws MotoAppException {
        Echipa echipa = echipeDBRepository.getEchipaByName(name);
        return echipa;
    }

    @Override
    public Cursa getCursaByCapacitate(int capacitate) throws MotoAppException {
        Cursa cursa = curseDBRepository.getCursaByCapacitate(capacitate);
        return cursa;
    }


}
