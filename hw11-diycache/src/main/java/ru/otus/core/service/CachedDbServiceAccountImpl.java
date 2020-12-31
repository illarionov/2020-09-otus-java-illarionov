package ru.otus.core.service;

import ru.otus.cachehw.HwCache;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;

import java.util.Optional;

public class CachedDbServiceAccountImpl implements DbServiceAccount {

    private static final String CACHE_KEY_PREFIX = CachedDbServiceAccountImpl.class.getSimpleName() + "-";

    private final DbServiceAccount delegate;

    private final HwCache<String, Account> clientCache;

    public CachedDbServiceAccountImpl(AccountDao accountDao, HwCache<String, Account> accountCache) {
        this(new DbServiceAccountImpl(accountDao), accountCache);
    }

    public CachedDbServiceAccountImpl(DbServiceAccount delegate, HwCache<String, Account> accountCache) {
        this.delegate = delegate;
        this.clientCache = accountCache;
    }
    
    @Override
    public String saveAccount(Account account) {
        String accountId = delegate.saveAccount(account);
        clientCache.put(getCacheKey(accountId), account);
        return accountId;
    }

    @Override
    public Optional<Account> getAccount(String id) {
        Account account = clientCache.computeIfAbsent(getCacheKey(id), accountId -> {
            return delegate.getAccount(id).orElse(null);
        });
        return Optional.of(account);
    }

    private String getCacheKey(String clientId) {
        return CACHE_KEY_PREFIX + clientId;
    }
}
