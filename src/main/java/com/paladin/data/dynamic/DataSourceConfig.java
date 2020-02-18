package com.paladin.data.dynamic;

import com.paladin.data.dynamic.DataSourceFacade.DataSourceType;

public class DataSourceConfig {
	
	/**
	 * 数据库名称，唯一
	 */
	private String name;

	/**
	 * 数据链接URL
	 */
	private String url;

	/**
	 * 用户
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 是否启用
	 */
	private boolean enabled = true;
	

	private DataSourceType type = DataSourceType.HIKARI;
	
	private boolean autoCommit =true;
	
	private String connectionTestQuery;
	
	// ------------- hikari ---------------
	
	private int maximumPoolSize = 5;
	
	private int minimumIdle = 5;
	
	private int idleTimeout = 600000;
	
	private int maxLifetime = 1800000;
	
	private int connectionTimeout = 30000;
	
	// ------------- druid ---------------
	
	private int maxActive = 15;
	
	private int initialSize = 5;
	
	private int minIdle = 5;
	
	private int maxWait = 30000;
	
	private boolean testOnBorrow = false;
	
	private boolean testOnReturn = false;
	
	private boolean testWhileIdle = true;
	
	private int timeBetweenEvictionRunsMillis = 600000;
	
	private int minEvictableIdleTimeMillis = 300000;
	
	private Boolean poolPreparedStatements;		// oracle 建议为true
	
	private int maxPoolPreparedStatementPerConnectionSize = -1;
	
	/**  
	 * 数据库名称，唯一  
	 */
	public String getName() {
		return name;
	}

	/**  
	 * 数据库名称，唯一  
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**  
	 * 数据链接URL  
	 */
	public String getUrl() {
		return url;
	}

	/**  
	 * 数据链接URL  
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**  
	 * 用户  
	 */
	public String getUsername() {
		return username;
	}

	/**  
	 * 用户  
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**  
	 * 密码  
	 */
	public String getPassword() {
		return password;
	}

	/**  
	 * 密码  
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public DataSourceType getType() {
		return type;
	}

	public void setType(DataSourceType type) {
		this.type = type;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public int getMinimumIdle() {
		return minimumIdle;
	}

	public void setMinimumIdle(int minimumIdle) {
		this.minimumIdle = minimumIdle;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getMaxLifetime() {
		return maxLifetime;
	}

	public void setMaxLifetime(int maxLifetime) {
		this.maxLifetime = maxLifetime;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getConnectionTestQuery() {
		return connectionTestQuery;
	}

	public void setConnectionTestQuery(String connectionTestQuery) {
		this.connectionTestQuery = connectionTestQuery;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	

	public int getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public Boolean getPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}
}
