package com.paladin.framework.core.configuration.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager implements CacheManager {

	@SuppressWarnings("rawtypes")
	private static ThreadLocal<RedisCache> caches = new ThreadLocal<>();

	// 超时，分钟
	private int expire = 30;

	private RedisTemplate<String, Object> redisTemplate;

	public RedisCacheManager(RedisTemplate<String, Object> redisTemplate, int expire) {
		this.redisTemplate = redisTemplate;
		this.expire = expire;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Cache<K, V> getCache(String key) throws CacheException {
		RedisCache cache = caches.get();
		if (cache == null) {
			cache = new RedisCache(redisTemplate, expire);
			caches.set(cache);
		}
		cache.setKeyPrefix(key);
		return cache;
	}

}
