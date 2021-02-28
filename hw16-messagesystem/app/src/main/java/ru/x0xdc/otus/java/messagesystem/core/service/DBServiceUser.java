package ru.x0xdc.otus.java.messagesystem.core.service;

import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(UserEntity user);

    Optional<UserEntity> getUser(long id);

    Optional<UserEntity> getUserByLogin(String login);

    List<UserEntity> findAllUsers();
}
