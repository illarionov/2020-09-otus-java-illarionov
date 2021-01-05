package ru.otus.jdbc.dao;

import ru.otus.core.dao.BaseDao;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

import java.util.Optional;

class JdbcBaseDao<T, K> implements BaseDao<T, K> {

    protected final JdbcMapper<T, K> mapper;

    protected final SessionManager sessionManager;

    JdbcBaseDao(SessionManager sessionManager, JdbcMapper<T, K> jdbcMapper) {
        this.mapper = jdbcMapper;
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<T> findById(K id) {
        return mapper.findById(id);
    }

    @Override
    public K insert(T object) {
        return mapper.insert(object);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
