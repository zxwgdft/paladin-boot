package com.paladin.data.dynamic;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DynamicDataSource implements DataSource {

	private static final ThreadLocal<String> currentDatesource = new ThreadLocal<>();

	public void setCurrentDataSource(String name) {
		currentDatesource.set(name);
	}

	public DataSource getCurrentDataSource() {
		String current = currentDatesource.get();
		return current != null ? DataSourceContainer.getRealDataSource(current) : null;
	}
	
	public String getCurrentDataSourceName() {
		return currentDatesource.get();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? null : ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		DataSource ds = getCurrentDataSource();
		if (ds != null)
			ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		DataSource ds = getCurrentDataSource();
		if (ds != null)
			ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? 0 : ds.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? null : ds.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? null : ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? false : ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? null : ds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		DataSource ds = getCurrentDataSource();
		return ds == null ? null : ds.getConnection(username, password);
	}

}
