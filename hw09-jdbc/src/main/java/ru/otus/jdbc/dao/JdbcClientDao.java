package ru.otus.jdbc.dao;

import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

import java.util.List;

public class JdbcClientDao extends JdbcBaseDao<Client, Long> implements ClientDao {

    public JdbcClientDao(SessionManager sessionManager, JdbcMapper<Client, Long> jdbcMapper) {
        super(sessionManager, jdbcMapper);
    }

    @Override
    public List<Client> findAll() {
        return mapper.findAll();
    }
}
