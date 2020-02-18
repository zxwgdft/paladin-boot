package com.paladin.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class JDBCHelper {

	public static <T> T queryList(String sql, DataSource dataSource, QueryHandler<T> handler) throws SQLException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			
			if(connection == null)
				throw new SQLException("无法获取到Connection，是否数据库已经启动或者配置错误！");
			
			statement = connection.createStatement();
			statement.setQueryTimeout(60);
			resultSet = statement.executeQuery(sql);

			return handler.handle(resultSet);
			
		}
		finally {
			if (resultSet != null)
				resultSet.close();

			if (statement != null)
				statement.close();

			if (connection != null)
				connection.close();
		}

	}
	
	
	public static interface QueryHandler<T>{
		
		public T handle(ResultSet resultSet) throws SQLException;
		
	}
	
}
