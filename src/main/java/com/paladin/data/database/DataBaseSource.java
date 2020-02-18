package com.paladin.data.database;

import javax.sql.DataSource;

import com.paladin.data.database.model.DataBase;

/**
 * <h2>数据库数据源</h2> 非真正数据源，只是一个封装组合，需要操作真正数据源则调用getRealDataSource进行操作
 * 
 * @author TontZhou
 * 
 */
public abstract class DataBaseSource {

	protected DataSource realDataSource;

	protected DataBaseConfig config;

	public DataBaseSource(DataBaseConfig config) {
		if (config == null)
			throw new IllegalArgumentException("Database Config can't be null");

		this.config = config;

		realDataSource = createRealDataSource();
	}

	/**
	 * 根据数据库配置创建一个真实的数据源，该方法会在被对象创建时调用
	 */
	protected abstract DataSource createRealDataSource();

	private boolean initialized = false;
	private boolean isError = false;
	private boolean isClosed = false;
	private final Object lock = new Object();

	/**
	 * 获取真正的数据源操作
	 * 
	 * @return
	 */
	public DataSource getRealDataSource() {

		init();

		if (isError)
			throw new RuntimeException("数据源异常");

		if (isClosed)
			throw new RuntimeException("数据源已经关闭");

		return realDataSource;
	}

	/**
	 * 初始化数据源
	 */
	public void init() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized) {
					try {
						initialize();
					} catch (Exception e) {
						e.printStackTrace();
						isError = true;
					}
					initialized = true;
				}
			}
		}
	}

	/**
	 * 关闭数据源
	 */
	public void close() {
		if (!isClosed) {
			synchronized (lock) {
				if (!isClosed) {
					try {
						destroy();
					} catch (Exception e) {
						e.printStackTrace();
						isError = true;
					}
					isClosed = true;
				}
			}
		}
	}

	/**
	 * 具体初始化操作
	 * 
	 * @return
	 */
	protected abstract boolean initialize();

	/**
	 * 具体关闭数据源操作
	 * 
	 * @return
	 */
	protected abstract boolean destroy();

	/**
	 * 获取数据库对象
	 * 
	 * @param refresh
	 *            是否实时获取
	 * @return
	 */
	public abstract DataBase getDataBase(boolean refresh);

	/**
	 * 测试连接
	 * 
	 * @return true 连接成功，false则失败
	 */
	public abstract boolean testConnect();

	/**
	 * 获取数据库配置
	 * 
	 * @return
	 */
	public DataBaseConfig getDataBaseConfig() {
		return config;
	}

}
