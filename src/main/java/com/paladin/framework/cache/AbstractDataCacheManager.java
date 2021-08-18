package com.paladin.framework.cache;

import com.paladin.framework.exception.SystemException;
import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.utils.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2021/3/19
 */
@Slf4j
public abstract class AbstractDataCacheManager implements ApplicationRunner, DataCacheManager {

    private Map<String, DataCacheWrapper> id2CacheMap;
    private Map<String, DataCacheWrapper> class2CacheMap;
    private Map<String, DataCacheWrapper> dataClass2CacheMap;

    private boolean initialized = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动数据缓存管理...");

        Map<String, DataCache> dataCacheBeanMap = SpringBeanHelper.getBeansByType(DataCache.class);

        if (dataCacheBeanMap == null || dataCacheBeanMap.size() == 0) {
            log.warn("没有找到任何DataCache实例");
            return;
        }

        Map<String, DataCacheWrapper> id2CacheMap = new HashMap<>();
        Map<String, DataCacheWrapper> class2CacheMap = new HashMap<>();
        Map<String, DataCacheWrapper> dataClass2CacheMap = new HashMap<>();


        for (Map.Entry<String, DataCache> entry : dataCacheBeanMap.entrySet()) {
            DataCache source = entry.getValue();
            String cacheId = source.getId();

            DataCacheWrapper cache = getDataCacheWrapper(source);

            if (id2CacheMap.containsKey(cacheId)) {
                log.warn("存在多个DataCache的ID为[" + cacheId + "]");
            }

            id2CacheMap.put(cacheId, cache);
            class2CacheMap.put(cache.getClass().getName(), cache);

            // 通过泛型查找缓存的数据对象
            Class dataClass = ReflectUtil.getSuperClassArgument(source.getClass(), DataCache.class, 0);
            if (dataClass != null) {
                if (dataClass2CacheMap.containsKey(dataClass.getName())) {
                    log.warn("存在多个DataCache的数据缓存对象为[" + dataClass + "]");
                }
                dataClass2CacheMap.put(dataClass.getName(), cache);
            }
        }

        this.id2CacheMap = id2CacheMap;
        this.class2CacheMap = class2CacheMap;
        this.dataClass2CacheMap = dataClass2CacheMap;

        initialized = true;
    }

    protected abstract DataCacheWrapper getDataCacheWrapper(DataCache source);

    public void reloadCache(String id) {
        DataCacheWrapper cache = id2CacheMap.get(id);
        if (cache != null) {
            cache.toLoad();
        } else {
            throw new SystemException(SystemException.CODE_ERROR_CODE, "找不到[ID:" + id + "]对应的DataCacheWrapper");
        }
    }

    public void reloadCache(Class clazz) {
        String key = clazz.getName();
        DataCacheWrapper cache = class2CacheMap.get(key);
        if (cache == null) {
            cache = dataClass2CacheMap.get(clazz.getName());
        }

        if (cache != null) {
            cache.toLoad();
        } else {
            throw new SystemException(SystemException.CODE_ERROR_CODE, "找不到[" + clazz + "]对应的DataCacheWrapper");
        }
    }

    @Override
    public <T> T getData(Class<T> clazz) {
        DataCacheWrapper dataCache = dataClass2CacheMap.get(clazz.getName());
        if (dataCache != null) {
            return (T) dataCache.getData();
        } else {
            if (initialized) {
                throw new SystemException(SystemException.CODE_ERROR_CODE, "找不到[" + clazz + "]对应的DataCacheWrapper");
            } else {
                throw new SystemException(SystemException.CODE_ERROR_CODE, "系统正在启动中");
            }
        }
    }

    @Override
    public Object getData(String cacheId) {
        DataCacheWrapper dataCache = id2CacheMap.get(cacheId);
        if (dataCache != null) {
            return dataCache.getData();
        } else {
            if (initialized) {
                throw new SystemException(SystemException.CODE_ERROR_CODE, "找不到[ID:" + cacheId + "]对应的DataCacheWrapper");
            } else {
                throw new SystemException(SystemException.CODE_ERROR_CODE, "系统正在启动中");
            }
        }
    }

    @Override
    public Object getDataOfCache(Class<? extends DataCache> clazz) {
        DataCacheWrapper dataCache = class2CacheMap.get(clazz.getName());
        if (dataCache != null) {
            return dataCache.getData();
        } else {
            if (initialized) {
                throw new SystemException(SystemException.CODE_ERROR_CODE, "找不到[" + clazz + "]对应的DataCacheWrapper");
            } else {
                throw new SystemException(SystemException.CODE_ERROR_CODE, "系统正在启动中");
            }
        }
    }

    @Override
    public <T> DataCacheWrapper<T> getDataCacheWrapper(Class<T> clazz) {
        return dataClass2CacheMap.get(clazz.getName());
    }

    @Override
    public DataCacheWrapper getDataCacheWrapper(String cacheId) {
        return id2CacheMap.get(cacheId);
    }

}
