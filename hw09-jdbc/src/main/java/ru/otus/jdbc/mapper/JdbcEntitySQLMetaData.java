package ru.otus.jdbc.mapper;

class JdbcEntitySQLMetaData implements EntitySQLMetaData {

    private final String selectAllSql;

    private final String selectByIdSql;

    private final String insertSql;

    private final String updateSql;

    JdbcEntitySQLMetaData(String selectAllSql, String selectByIdSql, String insertSql, String updateSql) {
        this.selectAllSql = selectAllSql;
        this.selectByIdSql = selectByIdSql;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
