package com.paladin.framework.core.configuration.shiro.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCache<K, V> implements Cache<K, V> {

	private String keyPrefix;

	private int expire = 30;

	private RedisTemplate<String, Object> redisTemplate;

	public RedisCache(RedisTemplate<String, Object> redisTemplate, int expire) {
		this.redisTemplate = redisTemplate;
		this.expire = expire;
	}

	private String getRedisKey(K key) {
		return keyPrefix + key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws CacheException {
		if (key == null) {
			return null;
		}

		try {
			return (V) redisTemplate.opsForValue().get(getRedisKey(key));
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		if (key == null) {
			return value;
		}

		try {
			redisTemplate.opsForValue().set(getRedisKey(key), value, expire, TimeUnit.MINUTES);
		} catch (Exception e) {
			throw new CacheException(e);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(K key) throws CacheException {
		if (key == null) {
			return null;
		}

		V value = null;
		String realKey = getRedisKey(key);

		try {
			value = (V) redisTemplate.opsForValue().get(realKey);
		} catch (Exception e) {
			throw new CacheException(e);
		}

		try {
			redisTemplate.delete(realKey);
		} catch (Exception e) {
			throw new CacheException(e);
		}

		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() throws CacheException {
		Set<String> keys = (Set<String>) keys();		
		if(keys == null || keys.size() == 0) {
			return;
		}
		
		try {
			redisTemplate.delete(keys);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		return (Set<K>) redisTemplate.keys(keyPrefix + "*");
	}

	@Override
	public int size() {
		throw new RuntimeException("not support");
	}

	@Override
	public Collection<V> values() {
		throw new RuntimeException("not support");
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

}
