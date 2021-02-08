package ru.x0xdc.otus.java.messagesystem.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.x0xdc.otus.java.messagesystem.core.sessionmanager.SessionManager;
import ru.x0xdc.otus.java.messagesystem.core.sessionmanager.SessionManagerException;

import javax.persistence.EntityManagerFactory;

@Repository
public class SessionManagerHibernate implements SessionManager {

    private DatabaseSessionHibernate databaseSession;

    private final SessionFactory sessionFactory;

    @Autowired
    public SessionManagerHibernate(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory == null) {
            throw new SessionManagerException("EntityManagerFactory is null");
        }
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public void beginSession() {
        try {
            databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().commit();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().rollback();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        if (databaseSession == null) {
            return;
        }
        Session session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()) {
            return;
        }

        Transaction transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            return;
        }

        try {
            databaseSession.close();
            databaseSession = null;
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSessionHibernate getCurrentSession() {
        checkSessionAndTransaction();
        return databaseSession;
    }

    private void checkSessionAndTransaction() {
        if (databaseSession == null) {
            throw new SessionManagerException("DatabaseSession not opened ");
        }
        Session session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()) {
            throw new SessionManagerException("Session not opened ");
        }

        Transaction transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            throw new SessionManagerException("Transaction not opened ");
        }
    }
}
