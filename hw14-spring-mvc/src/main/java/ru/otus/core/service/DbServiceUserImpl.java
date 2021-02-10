package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

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
    public long saveUser(User user) {
        return executeInSession(() -> {
            long userId = userDao.insertOrUpdate(user);
            logger.info("created user: {}", userId);
            return userId;
        });
    }

    @Override
    public Optional<User> getUser(long id) {
        return executeInSession(() -> {
            Optional<User> userOptional = userDao.findById(id);
            logger.info("user: {}", userOptional.orElse(null));
            return userOptional;
        });
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return executeInSession(() -> {
            Optional<User> userOptional = userDao.findByLogin(login);
            logger.info("user: {}", userOptional.orElse(null));
            return userOptional;
        });
    }

    @Override
    public List<User> findAllUsers() {
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
