package com.paladin.data.model;

import javax.persistence.Id;
import javax.persistence.Table;

import com.paladin.framework.common.BaseModel;

@Table(name = "db_connection")
public class DBConnection extends BaseModel{
	
	@Id
	private String name;
	
	private String url;
	
	private String type;
	
	private String userName;
	
	private String password;
	
	private String note;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
}
