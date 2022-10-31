package dev.andreagenovese.CodiceFiscale;

public class WrongLengthException extends RuntimeException{

    public WrongLengthException(String msg) {
        super(msg);
    }

}
