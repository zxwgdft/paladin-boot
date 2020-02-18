package com.paladin.framework.core.configuration.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.paladin.framework.core.configuration.shiro.ShiroProperties;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis实现共享session
 * 
 * @author TontoZhou
 * @since 2018年3月16日
 */
public class ShiroRedisSessionDAO implements SessionDAO {

	private static Logger logger = LoggerFactory.getLogger(ShiroRedisSessionDAO.class);

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

		if (logger.isDebugEnabled()) {
			logger.debug("添加session缓存：" + session);
		}
	}

	/**
	 * 清楚缓存session，清楚本地和redis中的缓存
	 * 
	 * @param session
	 */
	private void uncacheSession(Session session) {

		redisTemplate.delete(getRedisKey(session.getId()));

		if (logger.isDebugEnabled()) {
			logger.debug("移除Redis中session缓存：" + session);
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

		if (logger.isDebugEnabled()) {
			logger.debug("从Redis获取session缓存：" + cached);
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

		if (logger.isDebugEnabled()) {
			logger.debug("更新Redis中session缓存过期时间：" + sessionId);
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
			 * 如果是{@link ControlledSession}，则需要判断是否只是访问时间更新，如果是，再判断是否超过更新间隔，如果是，则进行时间更新
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
						if (controlledSession.previousLastAccessTime != null) {
							long previous = controlledSession.previousLastAccessTime.getTime();
							long now = controlledSession.getLastAccessTime().getTime();
							if (now - previous > shiroProperties.getAccessTimeUpdateInterval()) {
								updateCacheExpireTime(session.getId());
							}
						}
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
		// 暂时不需要该功能，
		throw new RuntimeException("暂时不提供该功能");
	}

	/**
	 * 可控制的session，对session内容的修改进行了判断，用于判断是否只是更新了session的最好访问时间
	 * 
	 * @author TontoZhou
	 * @since 2018年3月20日
	 */
	public static class ControlledSession extends SimpleSession {

		private static final long serialVersionUID = -6898216841518687491L;

		// 除lastAccessTime以外其他字段发生改变时为true
		private transient boolean isContentChanged;
		private transient Date previousLastAccessTime;

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

		public void setLastAccessTime(Date lastAccessTime) {
			previousLastAccessTime = getLastAccessTime();
			super.setLastAccessTime(lastAccessTime);
		}

		public void touch() {
			previousLastAccessTime = getLastAccessTime();
			super.touch();
		}
		
		public void contentChanged() {
			isContentChanged = true;
		}
	}

}
