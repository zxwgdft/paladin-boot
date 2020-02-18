package com.paladin.framework.core.configuration.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterSessionFactory implements SessionFactory{

	private static Logger logger = LoggerFactory.getLogger(ClusterSessionFactory.class);

	/*
	 * 使用 {@link com.paladin.configuration.ShiroRedisSessionDAO.ControlledSession}
	 * 控制session update次数
	 */
	@Override
	public Session createSession(SessionContext initData) {
		if (initData != null) {
			String host = initData.getHost();
			if (host != null) {

				if (logger.isDebugEnabled()) {
					logger.debug("创建ControlledSession[HOST:" + host + "]");
				}

				return new ShiroRedisSessionDAO.ControlledSession(host);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.info("创建ControlledSession[HOST:无]");
		}
		return new ShiroRedisSessionDAO.ControlledSession();
	}
	
}
