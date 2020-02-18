package com.paladin.framework.core.configuration.shiro.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.Pac4jConstants;

import com.paladin.framework.core.configuration.PaladinConstants;
import com.paladin.framework.core.configuration.shiro.ShiroCasProperties;

import io.buji.pac4j.engine.ShiroSecurityLogic;
import io.buji.pac4j.filter.SecurityFilter;

public class PaladinCasAuthenticationFilter extends PaladinFormAuthenticationFilter {

	private SecurityFilter securityFilter;

	private String loginTypeField;
	private String casLoginUrl;
	private String successUrl;

	public PaladinCasAuthenticationFilter(ShiroCasProperties shiroCasProperties, Config config) {
		super();

		this.loginTypeField = shiroCasProperties.getLoginTypeField();
		this.casLoginUrl = shiroCasProperties.getClientLoginUrl();
		this.successUrl = shiroCasProperties.getSuccessUrl();

		this.securityFilter = new SecurityFilter();
		this.securityFilter.setConfig(config);
		this.securityFilter.setClients("CasClient");
		this.securityFilter.setSecurityLogic(new ShiroSecurityLogic<Object, J2EContext>() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			protected void saveRequestedUrl(J2EContext context, List<Client> currentClients) {
				HttpServletRequest request = context.getRequest();
				if (casLoginUrl.equals(request.getRequestURI())) {
					context.getSessionStore().set(context, Pac4jConstants.REQUESTED_URL, successUrl);
					return;
				}
				super.saveRequestedUrl(context, currentClients);
			}

		});

		this.setSuccessUrl(shiroCasProperties.getSuccessUrl());
		this.setLoginUrl(shiroCasProperties.getLoginUrl());
	}

	public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
		if (isCas((HttpServletRequest) servletRequest)) {
			securityFilter.doFilter(servletRequest, servletResponse, filterChain);
		} else {
			super.doFilterInternal(servletRequest, servletResponse, filterChain);
		}
	}

	private boolean isCas(HttpServletRequest httpRequest) {
		// 登录方式现在有单点登录和本地登录，APP默认本地，PC默认单点。我们在session中存入登录类型
		if (casLoginUrl.equals(httpRequest.getRequestURI())) {
			return true;
		}

		String loginType = (String) httpRequest.getSession().getAttribute(loginTypeField);
		return PaladinConstants.LOGIN_TYPE_CAS.equals(loginType);
	}

	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		subject.getSession().setAttribute(loginTypeField, PaladinConstants.LOGIN_TYPE_LOCAL);
		return super.onLoginSuccess(token, subject, request, response);
	}

}
