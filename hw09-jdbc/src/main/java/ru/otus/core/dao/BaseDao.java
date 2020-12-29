package ru.otus.core.dao;

import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface BaseDao<T, K> {

    Optional<T> findById(K id);
    //List<Client> findAll();

    K insert(T object);

    //void update(T client);
    //long insertOrUpdate(T client);

    SessionManager getSessionManager();

}
