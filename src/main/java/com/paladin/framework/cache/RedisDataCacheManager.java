package com.paladin.framework.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于redis缓存版本号的方式的数据缓存管理
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
@Slf4j
public class RedisDataCacheManager extends AbstractDataCacheManager {

    private String keyPrefix = "_DCV_";
    private RedisTemplate<String, String> redisTemplate;

    public RedisDataCacheManager(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisDataCacheManager(String keyPrefix, RedisTemplate<String, String> redisTemplate) {
        this.keyPrefix = keyPrefix;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected DataCacheWrapper getDataCacheWrapper(DataCache source) {
        return new RedisDataCacheWrapper(source, redisTemplate, keyPrefix + source.getId());
    }


}
