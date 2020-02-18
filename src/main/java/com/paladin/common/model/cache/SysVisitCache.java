package com.paladin.common.model.cache;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class SysVisitCache {

	@Id
	@GeneratedValue(generator = "UUID")
	private String id;

	private String ip;

	private String code;

	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}