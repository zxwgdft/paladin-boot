package com.paladin.framework.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paladin")
public class PaladinProperties {
	/** Web 开关 */
	private boolean webEnabled = true;
	/** Mybatis 开关 */
	private boolean mybatisEnabled = true;
	/** SHIRO 开关 */
	private boolean shiroEnabled = true;
	/** CAS 开关 */
	private boolean casEnabled = false;
	/** DRUID 开关*/
	private boolean druidEnabled = false;
	/** 默认密码 */
	private String defaultPassword = "1";
	/** 是否集群 */
	private boolean cluster = false;

	public boolean isWebEnabled() {
		return webEnabled;
	}

	public void setWebEnabled(boolean webEnabled) {
		this.webEnabled = webEnabled;
	}

	public boolean isMybatisEnabled() {
		return mybatisEnabled;
	}

	public void setMybatisEnabled(boolean mybatisEnabled) {
		this.mybatisEnabled = mybatisEnabled;
	}

	public boolean isShiroEnabled() {
		return shiroEnabled;
	}

	public void setShiroEnabled(boolean shiroEnabled) {
		this.shiroEnabled = shiroEnabled;
	}

	public boolean isCasEnabled() {
		return casEnabled;
	}

	public void setCasEnabled(boolean casEnabled) {
		this.casEnabled = casEnabled;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	public boolean isCluster() {
		return cluster;
	}

	public void setCluster(boolean cluster) {
		this.cluster = cluster;
	}

	public boolean isDruidEnabled() {
		return druidEnabled;
	}

	public void setDruidEnabled(boolean druidEnabled) {
		this.druidEnabled = druidEnabled;
	}

}
