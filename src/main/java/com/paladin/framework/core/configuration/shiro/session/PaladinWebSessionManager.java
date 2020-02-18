package com.paladin.framework.core.configuration.shiro.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paladin.framework.core.configuration.shiro.ShiroProperties;

public class PaladinWebSessionManager extends DefaultWebSessionManager {

	private static Logger logger = LoggerFactory.getLogger(PaladinWebSessionManager.class);

	private String tokenField;	
	private boolean isCluster = false;

	public PaladinWebSessionManager(ShiroProperties shiroProperties) {
		super();
		isCluster = shiroProperties.isRedisEnabled();
		tokenField = shiroProperties.getTokenField();
		if (tokenField != null && tokenField.length() == 0) {
			tokenField = null;
		}
	}

	@Override
	public Serializable getSessionId(SessionKey key) {
		Serializable id = key.getSessionId();
		if (id == null && WebUtils.isWeb(key)) {
			ServletRequest request = WebUtils.getRequest(key);
			ServletResponse response = WebUtils.getResponse(key);
			if (tokenField != null) {
				String token = ((HttpServletRequest) request).getHeader(tokenField);
				// 优先考虑TOKEN机制
				if (token != null && token.length() != 0) {
					// 复制与父类代码
					request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
					request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
					// automatically mark it valid here. If it is invalid, the
					// onUnknownSession method below will be invoked and we'll remove the attribute
					// at that time.
					request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
					// always set rewrite flag - SHIRO-361
					request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());

					return token;
				} else {
					return getSessionId(request, response);
				}
			} else {
				return getSessionId(request, response);
			}
		}
		return id;
	}

	/**
	 * 重写检索session方法，在request上缓存session对象，从而减少session的读取
	 */
	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {

		if (!isCluster) {
			return super.retrieveSession(sessionKey);
		}

		Serializable sessionId = getSessionId(sessionKey);
		if (sessionId == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a session could not be found.", sessionKey);
			}

			return null;
		}

		/*
		 * 首先从request中获取session，否则从数据库中检索
		 */

		ServletRequest request = null;
		if (sessionKey instanceof WebSessionKey) {
			request = ((WebSessionKey) sessionKey).getServletRequest();
		}

		if (request != null) {
			Object s = request.getAttribute(sessionId.toString());
			if (s != null) {
				return (Session) s;
			}
		}

		Session s = retrieveSessionFromDataSource(sessionId);
		if (s == null) {
			// session ID was provided, meaning one is expected to be found, but
			// we couldn't find one:
			String msg = "Could not find session with ID [" + sessionId + "]";
			throw new UnknownSessionException(msg);
		}

		// 保存session到request
		if (request != null) {
			request.setAttribute(sessionId.toString(), s);
		}

		return s;
	}

	public String getTokenField() {
		return tokenField;
	}

	public void setTokenField(String tokenField) {
		this.tokenField = tokenField;
	}

	public boolean isCluster() {
		return isCluster;
	}

	public void setCluster(boolean isCluster) {
		this.isCluster = isCluster;
	}

}
