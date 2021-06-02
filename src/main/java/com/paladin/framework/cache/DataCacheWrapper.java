package com.paladin.framework.cache;

/**
 * 数据缓存包装类
 * <p>
 * 每个包装类对应一个DataCacheManager
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
public interface DataCacheWrapper<T> {

    /**
     * 当数据变化时应当触发该方法，告诉数据缓存需要重新加载了
     */
    void toLoad();

    /**
     * 获取缓存的数据
     */
    T getData();

}
