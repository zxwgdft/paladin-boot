package com.paladin.framework.core.configuration.shiro.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.http.adapter.HttpActionAdapter;

import com.paladin.framework.core.configuration.PaladinConstants;
import com.paladin.framework.core.configuration.shiro.ShiroCasProperties;

import io.buji.pac4j.engine.ShiroCallbackLogic;

public class PaladinShiroCallbackLogic<R, C extends J2EContext> extends ShiroCallbackLogic<R, C> {

	private String loginTypeField;
	private String errorUrl;
	private String casServerLoginUrl;
	private boolean isToCas = false;

	public PaladinShiroCallbackLogic(ShiroCasProperties shiroCasProperties) {
		super();
		this.loginTypeField = shiroCasProperties.getLoginTypeField();
		this.errorUrl = shiroCasProperties.getCasErrorUrl();
		this.casServerLoginUrl = shiroCasProperties.getCasServerLoginUrl();

		if (errorUrl == null || errorUrl.length() == 0) {
			errorUrl = casServerLoginUrl;
			isToCas = true;
		} else {
			if (errorUrl.indexOf("?") > -1) {
				errorUrl += "&casServerLoginUrl=" + casServerLoginUrl;
			} else {
				errorUrl += "?casServerLoginUrl=" + casServerLoginUrl;
			}
		}
	}

	protected HttpAction redirectToOriginallyRequestedUrl(final C context, final String defaultUrl) {
		HttpAction action = super.redirectToOriginallyRequestedUrl(context, defaultUrl);
		context.getRequest().getSession().setAttribute(loginTypeField, PaladinConstants.LOGIN_TYPE_CAS);
		return action;
	}

	protected R handleException(final Exception e, final HttpActionAdapter<R, C> httpActionAdapter, final C context) {
		if (httpActionAdapter == null || context == null) {
			throw runtimeException(e);
		} else if (e instanceof HttpAction) {
			final HttpAction action = (HttpAction) e;
			logger.debug("extra HTTP action required in security: {}", action.getCode());
			return httpActionAdapter.adapt(action.getCode(), context);
		} else {
			String url = errorUrl;
			if (!isToCas) {
				/**
				 * 多Realm下无法获取到准确Exception，需要改写ModularRealmAuthenticator中方法，保存每个Realm抛出的异常
				 */
				String message = "系统单点登录异常！";
				if (e instanceof DisabledAccountException) {
					message += "账号状态不可用，或被锁定。";
				} else if (e instanceof ExcessiveAttemptsException) {
					message += "短时间内登录次数过多，请稍后再试。";
				} else if (e instanceof UnknownAccountException) {
					message += "未找到对应用户，请联系相关负责人确认用户账号是否开启，或数据是否完整。";
				} else if (e instanceof ConcurrentAccessException) {
					message += "系统不允许同一账号多次登录。";
				} else if (e instanceof UnsupportedTokenException) {
					message += "不支持的Token凭票登录方式。";
				} else if (e instanceof CasAuthenticationException) {
					message += e.getMessage();
				}

				try {
					message =URLEncoder.encode(message, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
				}
				url = url + "&message=" + message;
			}

			final HttpAction action = HttpAction.redirect(context, url);
			return httpActionAdapter.adapt(action.getCode(), context);
		}
	}

}
