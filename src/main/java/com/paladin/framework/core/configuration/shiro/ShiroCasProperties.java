package com.paladin.framework.core.configuration.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.paladin.framework.core.GlobalProperties;

@ConfigurationProperties(prefix = "paladin.shiro")
public class ShiroCasProperties extends ShiroProperties{

	/**
	 * CAS 服务端URL
	 */
	private String casServerUrl = "http://172.16.11.135:8001/cas";	
	
	/**
	 * CAS 服务端登录URL
	 */
	private String casServerLoginUrl = "http://172.16.11.135:8001/cas/login";

	/**
	 * 客户端URL
	 */
	private String clientServerUrl = "http://172.16.11.135:8010";
	
	/**
	 * CAS 客户端登录URL
	 */
	private String clientLoginUrl = "/" + GlobalProperties.project + "/login/cas";
	
	/**
	 * CASFilter
	 */
	private String casFilterUrlPattern = "/" + GlobalProperties.project + "/cas";

	/**
	 * CAS 协议
	 */
	private String casProtocol = "CAS30";
	
	/**
	 * 
	 */
	private String casErrorUrl = "/static/html/error_cas_500.html";
	

	public String getCasServerUrl() {
		return casServerUrl;
	}

	public void setCasServerUrl(String casServerUrl) {
		this.casServerUrl = casServerUrl;
	}

	public String getCasServerLoginUrl() {
		return casServerLoginUrl;
	}

	public void setCasServerLoginUrl(String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}

	public String getClientServerUrl() {
		return clientServerUrl;
	}

	public void setClientServerUrl(String clientServerUrl) {
		this.clientServerUrl = clientServerUrl;
	}

	public String getClientLoginUrl() {
		return clientLoginUrl;
	}

	public void setClientLoginUrl(String clientLoginUrl) {
		this.clientLoginUrl = clientLoginUrl;
	}

	public String getCasFilterUrlPattern() {
		return casFilterUrlPattern;
	}

	public void setCasFilterUrlPattern(String casFilterUrlPattern) {
		this.casFilterUrlPattern = casFilterUrlPattern;
	}

	public String getCasProtocol() {
		return casProtocol;
	}

	public void setCasProtocol(String casProtocol) {
		this.casProtocol = casProtocol;
	}

	public String getCasErrorUrl() {
		return casErrorUrl;
	}

	public void setCasErrorUrl(String casErrorUrl) {
		this.casErrorUrl = casErrorUrl;
	}


	

}
