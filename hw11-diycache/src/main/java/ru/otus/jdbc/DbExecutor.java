package ru.otus.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public interface DbExecutor {

    <R> R executeInsert(Connection connection, String sql, List<Object> params) throws SQLException;

    <R> R executeSelect(Connection connection, String sql, List<Object> params, Function<ResultSet, R> rsMapper) throws SQLException;

}
