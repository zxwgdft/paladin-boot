package com.paladin.framework.core.configuration.shiro.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.config.Config;

import com.paladin.framework.core.configuration.PaladinConstants;
import com.paladin.framework.core.configuration.shiro.ShiroCasProperties;

import io.buji.pac4j.filter.LogoutFilter;

public class PaladinCasLogoutFilter extends PaladinLogoutFilter {

	private LogoutFilter casLogoutFilter;

	private String loginTypeField;

	public PaladinCasLogoutFilter(ShiroCasProperties shiroCasProperties, Config config) {
		loginTypeField = shiroCasProperties.getLoginTypeField();

		this.casLogoutFilter = new LogoutFilter();
		this.casLogoutFilter.setConfig(config);
		this.casLogoutFilter.setCentralLogout(true);
		this.casLogoutFilter.setLocalLogout(true);// 销毁本地

	}

	public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
		if (isCas((HttpServletRequest) servletRequest)) {
			casLogoutFilter.doFilter(servletRequest, servletResponse, filterChain);
		} else {
			super.doFilterInternal(servletRequest, servletResponse, filterChain);
		}
	}

	private boolean isCas(HttpServletRequest httpRequest) {
		String loginType = (String) httpRequest.getSession().getAttribute(loginTypeField);
		return PaladinConstants.LOGIN_TYPE_CAS.equals(loginType);
	}

}
