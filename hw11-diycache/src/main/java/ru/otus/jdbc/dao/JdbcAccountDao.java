package ru.otus.jdbc.dao;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

public class JdbcAccountDao extends JdbcBaseDao<Account, String> implements AccountDao {

    public JdbcAccountDao(SessionManager sessionManager, JdbcMapper<Account, String> jdbcMapper) {
        super(sessionManager, jdbcMapper);
    }

}
