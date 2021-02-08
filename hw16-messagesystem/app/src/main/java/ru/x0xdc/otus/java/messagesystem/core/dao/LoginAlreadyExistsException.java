package ru.x0xdc.otus.java.messagesystem.core.dao;

public class LoginAlreadyExistsException extends ConstraintViolationDaoException {
    public LoginAlreadyExistsException(Exception ex) {
        super("login", ex);
    }
}
