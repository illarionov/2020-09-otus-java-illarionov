package ru.x0xdc.otus.java.atm;

public class DispenseException extends RuntimeException {
    public DispenseException() {
    }

    public DispenseException(String message) {
        super(message);
    }

    public DispenseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DispenseException(Throwable cause) {
        super(cause);
    }
}
