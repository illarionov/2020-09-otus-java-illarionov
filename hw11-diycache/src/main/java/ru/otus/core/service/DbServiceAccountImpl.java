package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;

import java.util.Optional;
import java.util.concurrent.Callable;

public class DbServiceAccountImpl implements DbServiceAccount{

    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public String saveAccount(Account client) {
        String accountId = callInSession(() -> accountDao.insert(client));
        logger.info("created account: {}", accountId);
        return accountId;
    }

    @Override
    public Optional<Account> getAccount(String id) {
        Optional<Account> accountOptional = callInSession(() -> accountDao.findById(id));
        logger.info("account: {}", accountOptional.orElse(null));
        return accountOptional;
    }

    private <R> R callInSession(Callable<R> callable) throws DbServiceException {
        try (var sessionManager = accountDao.getSessionManager()) {
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
