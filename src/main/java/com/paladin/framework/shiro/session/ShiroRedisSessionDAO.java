package com.paladin.framework.shiro.session;

import com.paladin.framework.shiro.ShiroProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis实现共享session
 *
 * @author TontoZhou
 * @since 2018年3月16日
 */
@Slf4j
public class ShiroRedisSessionDAO implements SessionDAO {

    // session id generator
    private SessionIdGenerator sessionIdGenerator;

    private ShiroProperties shiroProperties;

    private RedisTemplate<String, Object> redisTemplate;

    public ShiroRedisSessionDAO(ShiroProperties shiroProperties, RedisTemplate<String, Object> redisTemplate) {
        this.sessionIdGenerator = new JavaUuidSessionIdGenerator();
        this.shiroProperties = shiroProperties;
        this.redisTemplate = redisTemplate;
    }

    // ------------------------------------------
    //
    // REDIS 操作缓存
    //
    // ------------------------------------------

    private String getRedisKey(Serializable sessionId) {
        return shiroProperties.getSessionPrefix() + sessionId.toString();
    }

    /**
     * 缓存session，放入本地缓存和redis缓存
     *
     * @param sessionId
     * @param session
     */
    private void cacheSessioin(Serializable sessionId, Session session) {

        redisTemplate.opsForValue().set(getRedisKey(sessionId), session, shiroProperties.getSessionTime(), TimeUnit.MINUTES);

        if (log.isDebugEnabled()) {
            log.debug("添加session缓存：" + session);
        }
    }

    /**
     * 清楚缓存session，清楚本地和redis中的缓存
     *
     * @param session
     */
    private void uncacheSession(Session session) {

        redisTemplate.delete(getRedisKey(session.getId()));

        if (log.isDebugEnabled()) {
            log.debug("移除Redis中session缓存：" + session);
        }
    }

    /**
     * 获取缓存session，先从本地缓存获取，如果没有则去redis获取
     *
     * @param sessionId
     * @return
     */
    private Session getCacheSession(Serializable sessionId) {

        Session cached = (Session) redisTemplate.opsForValue().get(getRedisKey(sessionId));

        if (log.isDebugEnabled()) {
            log.debug("从Redis获取session缓存：" + cached);
        }

        return cached;
    }

    /**
     * 更新过期时间
     *
     * @param sessionId
     */
    private void updateCacheExpireTime(Serializable sessionId) {

        redisTemplate.expire(getRedisKey(sessionId), shiroProperties.getSessionTime(), TimeUnit.MINUTES);

        if (log.isDebugEnabled()) {
            log.debug("更新Redis中session缓存过期时间：" + sessionId);
        }
    }

    // ------------------------------------------
    //
    // SessionDao implements
    //
    // ------------------------------------------

    @Override
    public Serializable create(Session session) {
        Serializable sessionId = this.sessionIdGenerator.generateId(session);
        ((SimpleSession) session).setId(sessionId);
        cacheSessioin(sessionId, session);
        return sessionId;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session session = getCacheSession(sessionId);
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {

        if (session instanceof ControlledSession) {

            /*
             */
            ControlledSession controlledSession = (ControlledSession) session;
            if (controlledSession.isValid()) {
                // TODO 需要考虑改变了session中object中值，而并没有调用ControlledSession中方法时isContentChanged值不会改变，所以不会更新问题
                if (controlledSession.isContentChanged) {
                    cacheSessioin(session.getId(), session);
                    controlledSession.isContentChanged = false;
                } else {
                    if (shiroProperties.getAccessTimeUpdateInterval() <= 0) {
                        cacheSessioin(session.getId(), session);
                    } else {
                        updateCacheExpireTime(session.getId());
                    }
                }
            } else {
                uncacheSession(session);
            }
        } else {
            if (session instanceof ValidatingSession) {
                if (((ValidatingSession) session).isValid()) {
                    cacheSessioin(session.getId(), session);
                } else {
                    uncacheSession(session);
                }
            } else {
                cacheSessioin(session.getId(), session);
            }
        }
    }

    @Override
    public void delete(Session session) {
        uncacheSession(session);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        // 该方法用于验证session过期线程，而redis自带过期功能，所以不需要。
        return null;
    }

    /**
     * 可控制的session，对session内容的修改进行了判断，用于判断是否只是更新了session的最后访问时间
     *
     */
    public static class ControlledSession extends SimpleSession {

        // 除lastAccessTime以外其他字段发生改变时为true
        private transient boolean isContentChanged;

        public ControlledSession() {
            super();
            isContentChanged = true;
        }

        public ControlledSession(String host) {
            super(host);
            isContentChanged = true;
        }

        @Override
        public void setId(Serializable id) {
            super.setId(id);
            isContentChanged = true;
        }

        @Override
        public void setStopTimestamp(Date stopTimestamp) {
            super.setStopTimestamp(stopTimestamp);
            isContentChanged = true;
        }

        @Override
        public void setExpired(boolean expired) {
            super.setExpired(expired);
            isContentChanged = true;
        }

        @Override
        public void setTimeout(long timeout) {
            super.setTimeout(timeout);
            isContentChanged = true;
        }

        @Override
        public void setHost(String host) {
            super.setHost(host);
            isContentChanged = true;
        }

        @Override
        public void setAttributes(Map<Object, Object> attributes) {
            super.setAttributes(attributes);
            isContentChanged = true;
        }

        @Override
        public void setAttribute(Object key, Object value) {
            super.setAttribute(key, value);
            isContentChanged = true;
        }

        @Override
        public Object removeAttribute(Object key) {
            isContentChanged = true;
            return super.removeAttribute(key);
        }

        public void contentChanged() {
            isContentChanged = true;
        }
    }

}
