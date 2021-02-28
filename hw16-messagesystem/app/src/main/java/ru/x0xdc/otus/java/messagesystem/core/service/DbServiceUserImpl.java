package ru.x0xdc.otus.java.messagesystem.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.x0xdc.otus.java.messagesystem.core.dao.UserDao;
import ru.x0xdc.otus.java.messagesystem.core.sessionmanager.SessionManager;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@Service
public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    @Autowired
    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(UserEntity user) {
        return executeInSession(() -> {
            long userId = userDao.insertOrUpdate(user);
            logger.info("created user: {}", userId);
            return userId;
        });
    }

    @Override
    public Optional<UserEntity> getUser(long id) {
        return executeInSession(() -> {
            Optional<UserEntity> userOptional = userDao.findById(id);
            logger.info("user: {}", userOptional.orElse(null));
            return userOptional;
        });
    }

    @Override
    public Optional<UserEntity> getUserByLogin(String login) {
        return executeInSession(() -> {
            Optional<UserEntity> userOptional = userDao.findByLogin(login);
            logger.info("user: {}", userOptional.orElse(null));
            return userOptional;
        });
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return executeInSession(userDao::findAll);
    }

    private <T> T executeInSession(Callable<T> callable) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return callable.call();
            } catch (Exception e) {
                try {
                    sessionManager.rollbackSession();
                } catch (Exception e2) {
                    e.addSuppressed(e2);
                }
                throw new DbServiceException(e);
            }
        }
    }
}
