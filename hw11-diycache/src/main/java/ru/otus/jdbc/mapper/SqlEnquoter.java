package ru.otus.jdbc.mapper;

public interface SqlEnquoter {

    /**
     * @see  java.sql.Statement#enquoteIdentifier
     */
    String enquoteIdentifier(String identifier) throws JdbcMapperException;
    
}
