package motoapp.services;

import motoapp.model.Echipa;
import motoapp.persistence.EchipeDBRepository;

import java.util.List;

public class EchipaService {

    private EchipeDBRepository repository;

    public EchipaService(EchipeDBRepository repository) {
        this.repository = repository;
    }

    public List<String> getNumeEchipe(){
        return repository.getNumeEchipe();
    }

    public Echipa getEchipaDupaNume(String nume){
        return repository.getEchipaByName(nume);
    }
}
