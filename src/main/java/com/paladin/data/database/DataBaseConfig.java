package com.paladin.data.database;

/**
 * <h2>数据库配置</h2>
 * @author TontZhou
 *
 */
public class DataBaseConfig implements Cloneable{
	
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
	 * 数据库类型
	 */
	private DataBaseType type;
	

	public DataBaseType getType() {
		return type;
	}

	public void setType(DataBaseType type) {
		this.type = type;
	}

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


	@Override  
    public DataBaseConfig clone() throws CloneNotSupportedException {  
        return (DataBaseConfig) super.clone();  
    } 
	
	
	
	//待扩展
}
