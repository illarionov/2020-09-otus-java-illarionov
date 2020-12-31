package ru.otus.core.service;

import ru.otus.cachehw.HwCache;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;

import java.util.List;
import java.util.Optional;

public class CachedDbServiceClientImpl implements DBServiceClient {

    private static final String CACHE_KEY_PREFIX = DbServiceClientImpl.class.getSimpleName() + "-";

    private final DBServiceClient delegate;

    private final HwCache<String, Client> clientCache;

    public CachedDbServiceClientImpl(ClientDao clientDao, HwCache<String, Client> clientCache) {
        this(new DbServiceClientImpl(clientDao), clientCache);
    }

    public CachedDbServiceClientImpl(DBServiceClient delegate, HwCache<String, Client> clientCache) {
        this.delegate = delegate;
        this.clientCache = clientCache;
    }

    @Override
    public long saveClient(Client client) {
        long clientId = delegate.saveClient(client);
        clientCache.put(getCacheKey(clientId), client);
        return clientId;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = clientCache.computeIfAbsent(getCacheKey(id), clientId -> {
            return delegate.getClient(id).orElse(null);
        });
        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        return delegate.findAll();
    }

    private String getCacheKey(long clientId) {
        return CACHE_KEY_PREFIX + clientId;
    }
}
