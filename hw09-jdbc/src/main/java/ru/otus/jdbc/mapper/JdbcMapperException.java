package ru.otus.jdbc.mapper;

public class JdbcMapperException extends RuntimeException {
    public JdbcMapperException() {
    }

    public JdbcMapperException(String message) {
        super(message);
    }

    public JdbcMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcMapperException(Throwable cause) {
        super(cause);
    }
}
