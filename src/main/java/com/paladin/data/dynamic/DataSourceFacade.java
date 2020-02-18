package com.paladin.data.dynamic;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFacade {

	public static enum DataSourceType {
		HIKARI, DRUID;
	}

	protected String name;
	protected boolean enabled;
	protected DataSource realDataSource;
	protected DataSourceConfig config;
	protected DataSourceType type = DataSourceType.HIKARI;

	public DataSourceFacade(DataSourceConfig config) {
		if (config == null) {
			throw new IllegalArgumentException("DataSource Config can't be null");
		}

		this.config = config;
		this.name = config.getName();
		this.type = config.getType();

		setEnabled(config.getEnabled());
	}

	/**
	 * 根据数据库配置创建一个真实的数据源，该方法会在被对象创建时调用
	 */
	private DataSource createRealDataSource() {
		if (type == DataSourceType.DRUID) {
			return createDruidDataSource();
		}
		return createHikariDataSource();
	}

	private DataSource createDruidDataSource() {

		boolean isOracle = config.getUrl().contains("oracle");

		DruidDataSource dataSource = new DruidDataSource();

		dataSource.setUrl(config.getUrl());
		dataSource.setPassword(config.getPassword());
		dataSource.setUsername(config.getUsername());
		dataSource.setName(config.getName());

		dataSource.setDefaultAutoCommit(config.isAutoCommit());
		dataSource.setMaxActive(config.getMaxActive());
		dataSource.setInitialSize(config.getInitialSize());
		dataSource.setMinIdle(config.getMinIdle());
		dataSource.setMaxWait(config.getMaxWait());
		dataSource.setTestOnBorrow(config.isTestOnBorrow());
		dataSource.setTestOnReturn(config.isTestOnReturn());
		dataSource.setTestWhileIdle(config.isTestWhileIdle());
		dataSource.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
		dataSource.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());

		Boolean poolPreparedStatements = config.getPoolPreparedStatements();
		int maxPoolPreparedStatementPerConnectionSize =  config.getMaxPoolPreparedStatementPerConnectionSize();
		if (poolPreparedStatements == null) {
			poolPreparedStatements = isOracle ? true : false;
		}

		if(maxPoolPreparedStatementPerConnectionSize <= 0 && poolPreparedStatements) {
			maxPoolPreparedStatementPerConnectionSize = 20;
		}
		
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

		String connectionTestQuery = config.getConnectionTestQuery();
		if (connectionTestQuery == null) {
			if (isOracle) {
				connectionTestQuery = "select 1 from dual";
			} else {
				connectionTestQuery = "select 1";
			}
		}

		dataSource.setValidationQuery(connectionTestQuery);
		return dataSource;
	}

	private HikariDataSource createHikariDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(config.getUrl());
		hikariConfig.setUsername(config.getUsername());
		hikariConfig.setPassword(config.getPassword());
		hikariConfig.setMinimumIdle(config.getMinimumIdle());
		hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
		hikariConfig.setAutoCommit(config.isAutoCommit());
		hikariConfig.setIdleTimeout(config.getIdleTimeout());
		hikariConfig.setMaxLifetime(config.getMaxLifetime());
		hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
		
		String connectionTestQuery = config.getConnectionTestQuery();

		if (connectionTestQuery == null) {
			if (config.getUrl().contains("oracle")) {
				connectionTestQuery = "select 1 from dual";
			} else {
				connectionTestQuery = "select 1";
			}
		}

		hikariConfig.setConnectionTestQuery(connectionTestQuery);
		return new HikariDataSource(hikariConfig);
	}

	/**
	 * 设置是否启用
	 * 
	 * @param enabled
	 */
	public synchronized void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			if (this.enabled) {
				// 关闭
				if (realDataSource != null) {
					if (type == DataSourceType.DRUID) {
						((DruidDataSource) realDataSource).close();
					} else {
						((HikariDataSource) realDataSource).close();
					}
					realDataSource = null;
				}
				this.enabled = false;
			} else {
				// 打开
				if (realDataSource != null) {
					// 如果有数据源尝试关闭
					if (type == DataSourceType.DRUID) {
						((DruidDataSource) realDataSource).close();
					} else {
						((HikariDataSource) realDataSource).close();
					}
					realDataSource = null;
				}
				realDataSource = createRealDataSource();
				this.enabled = true;
			}
		}
	}

	/**
	 * 是否启用
	 * 
	 * @return
	 */
	public synchronized boolean isEnabled() {
		return enabled;
	}

	/**
	 * 获取数据库配置
	 * 
	 * @return
	 */
	public DataSourceConfig getDataBaseConfig() {
		return config;
	}

	/**
	 * 获取真正的数据源操作
	 * 
	 * @return
	 */
	public DataSource getRealDataSource() {
		return realDataSource;
	}

	/**
	 * 数据源名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
}
