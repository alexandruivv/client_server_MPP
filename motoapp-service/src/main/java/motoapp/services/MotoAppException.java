package motoapp.services;

public class MotoAppException extends Exception {
    public MotoAppException() {

    }

    public MotoAppException(String msg) {
        super(msg);
    }

    public MotoAppException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
