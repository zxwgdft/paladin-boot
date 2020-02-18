package com.paladin.common.model.syst;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.paladin.framework.common.UnDeleteBaseModel;

public class SysUser extends UnDeleteBaseModel implements Serializable {

	private static final long serialVersionUID = -1534400185542562200L;

	/**
	 * 启用状态
	 */
	public final static Integer STATE_ENABLED = 1;
	/**
	 * 停用状态
	 */
	public final static Integer STATE_DISABLED = 0;

	/**
	 * 管理员账号
	 */
	public final static Integer TYPE_ADMIN = 1;
	/**
	 * 应用管理员账号
	 */
	public final static Integer TYPE_APP_ADMIN = 3;


	public final static String COLUMN_FIELD_ACCOUNT = "account";

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	private String id;
	private String account;
	private String password;
	private String salt;
	private String userId;
	private Integer state;
	private Integer type;
	private Date lastLoginTime;
	private Integer isFirstLogin = 1;

	public Integer getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(Integer isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

}
