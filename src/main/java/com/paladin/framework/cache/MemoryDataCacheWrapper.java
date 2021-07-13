package com.paladin.framework.cache;

/**
 * For MemoryDataCacheManager
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
public class MemoryDataCacheWrapper<T> implements DataCacheWrapper<T> {

    private DataCache<T> source;
    private T data;
    private long version = 0;
    private volatile boolean loaded = false;

    public MemoryDataCacheWrapper(DataCache<T> dataCache) {
        source = dataCache;
    }

    public void toLoad() {
        synchronized (source) {
            version++;
            loaded = false;
        }
    }

    public T getData() {
        if (!loaded) {
            // 为了性能只做到load过程的同步，但不保证返回data时候loaded为true
            synchronized (source) {
                if (!loaded) {
                    data = source.loadData(version);
                    loaded = true;
                }
            }
        }
        return data;
    }

}
