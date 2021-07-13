package com.paladin.framework.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author TontoZhou
 * @since 2021/3/19
 */
@Slf4j
public class RedisDataCacheWrapper<T> implements DataCacheWrapper<T> {

    private RedisTemplate<String, String> redisTemplate;
    private DataCache<T> source;
    private volatile long version = -1;
    private String cacheKey;
    private T data;

    public RedisDataCacheWrapper(DataCache<T> dataCache, RedisTemplate<String, String> redisTemplate, String cacheKey) {
        this.cacheKey = cacheKey;
        this.source = dataCache;
        this.redisTemplate = redisTemplate;
    }

    public void toLoad() {
        try {
            redisTemplate.opsForValue().increment(cacheKey);
        } catch (Exception e) {
            log.error("数据缓存异常！", e);
            synchronized (source) {
                version = -1;
            }
        }
    }

    public T getData() {
        try {
            long current = getVersion();
            if (current != version) {
                synchronized (source) {
                    current = getVersion();
                    if (version != current) {
                        if (current > version) {
                            data = source.loadData(current);
                            version = current;
                        } else {
                            data = source.loadData(version);
                            redisTemplate.opsForValue().set(cacheKey, String.valueOf(version));
                        }
                    }
                }
            }
            return data;
        } catch (Exception e) {
            log.error("数据缓存异常！", e);
            return source.loadData(version);
        }
    }

    private long getVersion() {
        String value = redisTemplate.opsForValue().get(cacheKey);
        return value == null ? 0 : Long.valueOf(value);
    }

}
