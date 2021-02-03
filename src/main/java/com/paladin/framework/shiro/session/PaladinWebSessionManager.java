package com.paladin.framework.shiro.session;

import com.paladin.framework.shiro.ShiroProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Slf4j
public class PaladinWebSessionManager extends DefaultWebSessionManager {

    private String tokenField;
    // 是否redis持久化session
    private boolean isRedis = false;

    public PaladinWebSessionManager(ShiroProperties shiroProperties) {
        super();
        isRedis = shiroProperties.isRedisEnabled();
        tokenField = shiroProperties.getTokenField();
        if (tokenField != null && tokenField.length() == 0) {
            tokenField = null;
        }
        setSessionValidationSchedulerEnabled(shiroProperties.isSessionValidationSchedulerEnabled());
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
                    request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
                    request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
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

        if (!isRedis) {
            return super.retrieveSession(sessionKey);
        }

        Serializable sessionId = getSessionId(sessionKey);
        if (sessionId == null) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a session could not be found.", sessionKey);
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
            Object s = request.getAttribute("_sc_" + sessionId.toString());
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
            request.setAttribute("_sc_" + sessionId.toString(), s);
        }

        return s;
    }

}
