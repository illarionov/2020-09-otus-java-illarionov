package ru.otus.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.function.Function;

public class DbExecutorImpl implements DbExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

    @Override
    public <R> R executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        logger.debug("executeInsert() sql: `{}`, params: `{}`", sql, params);

        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                //noinspection unchecked
                return (R) rs.getObject(1);
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public <R> R executeSelect(Connection connection, String sql, List<Object> params, Function<ResultSet, R> rsMapper) throws SQLException {
        logger.debug("executeSelect() sql: `{}`, params: `{}`", sql, params);
        try (var pst = connection.prepareStatement(sql)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            try (var rs = pst.executeQuery()) {
                return rsMapper.apply(rs);
            }
        }
    }
}
