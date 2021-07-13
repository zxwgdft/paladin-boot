package com.paladin.data.dynamic;

import com.paladin.framework.exception.SystemException;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DynamicJdbcTransaction implements Transaction {

    private Map<String, JdbcTransaction> jdbcTransactionMap = new HashMap<>();

    protected DynamicDataSource dataSource;
    protected TransactionIsolationLevel level;
    protected boolean autoCommmit;

    public DynamicJdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
        dataSource = (DynamicDataSource) ds;
        level = desiredLevel;
        autoCommmit = desiredAutoCommit;
    }

    protected JdbcTransaction getCurrentTransaction() {
        DataSource realSource = dataSource.getCurrentDataSource();
        String name = dataSource.getCurrentDataSourceName();

        if (dataSource == null) {
            throw new SystemException(SystemException.CODE_ERROR_CONFIG, "找不到当前数据库");
        }

        JdbcTransaction transaction = jdbcTransactionMap.get(name);
        if (transaction == null) {
            transaction = new JdbcTransaction(realSource, level, autoCommmit);
            jdbcTransactionMap.put(name, transaction);
        }

        return transaction;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getCurrentTransaction().getConnection();
    }

    @Override
    public void commit() throws SQLException {
        for (JdbcTransaction transaction : jdbcTransactionMap.values()) {
            transaction.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        for (JdbcTransaction transaction : jdbcTransactionMap.values()) {
            transaction.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        for (JdbcTransaction transaction : jdbcTransactionMap.values()) {
            transaction.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }

}
