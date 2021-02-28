package ru.x0xdc.otus.java.messagesystem.core.dao;

public class UserDaoException extends RuntimeException {
    public UserDaoException(Exception ex) {
        super(ex);
    }
}
