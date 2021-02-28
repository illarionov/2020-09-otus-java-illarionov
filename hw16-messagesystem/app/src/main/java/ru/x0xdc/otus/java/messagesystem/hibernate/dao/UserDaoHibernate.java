package ru.x0xdc.otus.java.messagesystem.hibernate.dao;


import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.x0xdc.otus.java.messagesystem.core.dao.ConstraintViolationDaoException;
import ru.x0xdc.otus.java.messagesystem.core.dao.UserDao;
import ru.x0xdc.otus.java.messagesystem.core.dao.UserDaoException;
import ru.x0xdc.otus.java.messagesystem.core.sessionmanager.SessionManager;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;
import ru.x0xdc.otus.java.messagesystem.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.x0xdc.otus.java.messagesystem.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoHibernate implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    @Autowired
    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<UserEntity> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(UserEntity.class, id));
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Query<UserEntity> query = currentSession.getHibernateSession().createQuery(
                    "SELECT a FROM UserEntity a WHERE a.login = :login", UserEntity.class);
            query.setParameter("login", login);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Query<UserEntity> query = currentSession.getHibernateSession().createQuery(
                    "SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.address LEFT JOIN FETCH u.phones ORDER BY u.id", UserEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insert(UserEntity user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            throw createModificationUserDaoException(e);
        }
    }

    @Override
    public void update(UserEntity user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            throw createModificationUserDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(UserEntity user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getId();
        } catch (Exception e) {
            throw createModificationUserDaoException(e);
        }
    }

    private static UserDaoException createModificationUserDaoException(Exception e) throws UserDaoException {
        if (e instanceof PersistenceException
                && e.getCause() instanceof ConstraintViolationException) {
            return ConstraintViolationDaoException.create(((ConstraintViolationException) e.getCause()).getConstraintName(), e);
        } else {
            return new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
