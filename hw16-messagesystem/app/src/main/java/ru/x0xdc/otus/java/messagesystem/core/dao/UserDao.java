package ru.x0xdc.otus.java.messagesystem.core.dao;

import ru.x0xdc.otus.java.messagesystem.core.sessionmanager.SessionManager;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserDao {
    Optional<UserEntity> findById(long id);

    Optional<UserEntity> findByLogin(String login);

    List<UserEntity> findAll();

    long insert(UserEntity user);

    void update(UserEntity user);

    long insertOrUpdate(UserEntity user);

    SessionManager getSessionManager();
}
