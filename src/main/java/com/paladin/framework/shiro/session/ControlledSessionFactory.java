package com.paladin.framework.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

@Slf4j
public class ControlledSessionFactory implements SessionFactory {

    /*
     * 使用 {@link com.paladin.configuration.ShiroRedisSessionDAO.ControlledSession}
     * 控制session update次数
     */
    @Override
    public Session createSession(SessionContext initData) {
        if (initData != null) {
            String host = initData.getHost();
            if (host != null) {

                if (log.isDebugEnabled()) {
                    log.debug("创建ControlledSession[HOST:" + host + "]");
                }

                return new ShiroRedisSessionDAO.ControlledSession(host);
            }
        }

        if (log.isDebugEnabled()) {
            log.info("创建ControlledSession[HOST:无]");
        }
        return new ShiroRedisSessionDAO.ControlledSession();
    }

}
