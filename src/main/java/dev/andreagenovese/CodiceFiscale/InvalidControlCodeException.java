package dev.andreagenovese.CodiceFiscale;

public class InvalidControlCodeException extends RuntimeException {

    public InvalidControlCodeException(String msg) {
        super(msg);
    }

}
