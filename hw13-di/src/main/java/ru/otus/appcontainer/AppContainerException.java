package ru.otus.appcontainer;

public class AppContainerException extends RuntimeException {
    public AppContainerException() {
    }

    public AppContainerException(String message) {
        super(message);
    }

    public AppContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppContainerException(Throwable cause) {
        super(cause);
    }
}
