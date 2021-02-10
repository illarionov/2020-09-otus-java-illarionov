package ru.otus.core.dao;

public class LoginAlreadyExistsException extends ConstraintViolationDaoException {
    public LoginAlreadyExistsException(Exception ex) {
        super("login", ex);
    }
}
