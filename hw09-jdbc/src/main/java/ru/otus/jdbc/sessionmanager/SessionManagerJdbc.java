package ru.otus.jdbc.sessionmanager;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sessionmanager.SessionManagerException;
import ru.otus.jdbc.mapper.JdbcMapperException;
import ru.otus.jdbc.mapper.SqlEnquoter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SessionManagerJdbc implements SessionManager {

    private static final int TIMEOUT_IN_SECONDS = 5;
    private final DataSource dataSource;
    private Connection connection;
    private DatabaseSessionJdbc databaseSession;
    private final SqlEnquoter sqlEnquoter;
    private Statement enquoterStatement;

    public SessionManagerJdbc(DataSource dataSource) {
        if (dataSource == null) {
            throw new SessionManagerException("Datasource is null");
        }
        this.dataSource = dataSource;
        this.sqlEnquoter = new JdbcSqlEnquoter();
    }

    @Override
    public void beginSession() {
        try {
            connection = dataSource.getConnection();
            databaseSession = new DatabaseSessionJdbc(connection);
            enquoterStatement = connection.createStatement();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        checkConnection();
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkConnection();
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        checkConnection();
        try {
            enquoterStatement.close();
            connection.close();
            enquoterStatement = null;
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSessionJdbc getCurrentSession() {
        checkConnection();
        return databaseSession;
    }

    public SqlEnquoter getSqlEnquoter() {
        return sqlEnquoter;
    }

    private void checkConnection() {
        try {
            if (connection == null || !connection.isValid(TIMEOUT_IN_SECONDS)) {
                throw new SessionManagerException("Connection is invalid");
            }
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }

    private class JdbcSqlEnquoter implements SqlEnquoter {

        @Override
        public String enquoteIdentifier(String identifier) throws JdbcMapperException {
            if (enquoterStatement == null) {
                throw new IllegalStateException("Session closed");
            }
            try {
                return enquoterStatement.enquoteIdentifier(identifier, false);
            } catch (SQLException exception) {
                throw new JdbcMapperException("Illegal identifier `" + identifier + "`", exception);
            }
        }

    }
}
