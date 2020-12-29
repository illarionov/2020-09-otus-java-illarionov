package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientDao clientDao;

    public DbServiceClientImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public long saveClient(Client client) {
        long clientId = callInSession(() -> clientDao.insert(client));
        logger.info("created client: {}", clientId);
        return clientId;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> clientOptional = callInSession(() -> clientDao.findById(id));
        logger.info("client: {}", clientOptional.orElse(null));
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients =  callInSession(clientDao::findAll);
        logger.info("clients: {}", clients);
        return clients;
    }

    private <R> R callInSession(Callable<R> callable) throws DbServiceException {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                R result = callable.call();
                sessionManager.commitSession();
                return result;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        } catch (DbServiceException dbServiceException) {
            throw dbServiceException;
        } catch (Exception e) {
            throw new DbServiceException(e);
        }
    }
}
