package com.paladin.common.core.cache;

import com.paladin.framework.cache.DataCacheManager;
import com.paladin.framework.cache.DataCacheWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author TontoZhou
 * @since 2021/6/4
 */
@Component
public class DataCacheHelper {

    @Autowired
    private DataCacheManager cacheManager;

    private static DataCacheManager instance;

    @PostConstruct
    public void init() {
        instance = cacheManager;
    }

    public static Object getData(String cacheId) {
        return instance.getData(cacheId);
    }

    public static <T> T getData(Class<T> clazz) {
        return instance.getData(clazz);
    }

    public static void reloadCache(Class clazz) {
        instance.reloadCache(clazz);
    }

    public static void reloadCache(String cacheId) {
        instance.reloadCache(cacheId);
    }

    public static <T> DataCacheWrapper<T> getDataCacheWrapper(Class<T> clazz) {
        return instance.getDataCacheWrapper(clazz);
    }

    public static DataCacheWrapper getDataCacheWrapper(String cacheId) {
        return instance.getDataCacheWrapper(cacheId);
    }

}
