package motoapp.services;

import motoapp.model.Operator;
import motoapp.persistence.OperatoriDBRepository;

public class LoginService {
    private OperatoriDBRepository repository;

    public LoginService(OperatoriDBRepository repository){
        this.repository = repository;
    }

    public boolean login(String username, String password){
        return repository.operatorExists(new Operator(0, username, password));
    }
}
