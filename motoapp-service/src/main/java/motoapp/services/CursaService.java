package motoapp.services;

import motoapp.model.Cursa;
import motoapp.persistence.CurseDBRepository;

import java.util.List;

public class CursaService {
    private CurseDBRepository repository;

    public CursaService(CurseDBRepository repository) {
        this.repository = repository;
    }

    public List<String> getCapacitati(){
        return repository.getCapacitati();
    }

    public Cursa getCursaDupaCapacitate(String capacitate){
        return repository.getCursaByCapacitate(Integer.parseInt(capacitate));
    }
}
