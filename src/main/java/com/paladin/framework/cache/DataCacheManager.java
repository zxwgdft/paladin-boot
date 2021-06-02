package com.paladin.framework.cache;

/**
 * @author TontoZhou
 * @since 2021/3/19
 */
public interface DataCacheManager {


    /**
     * 数据变化，从新读取数据
     *
     * @param id 数据缓存ID
     */
    void reloadCache(String id);

    /**
     * 数据变化，从新读取数据
     *
     * @param clazz 缓存的数据类型，可以为DataCache的类型或缓存数据的类型
     */
    void reloadCache(Class clazz);

    /**
     * 通过缓存数据类型获取缓存数据
     * <p>
     * 该缓存数据类型T必须有且仅有一个对于的DataCache，并且T不能为List、Map等则不适合用该方法，
     * 因为无法判断泛型具体类型，可使用通过cacheId获取数据
     *
     * @param clazz 缓存数据class
     * @return 类型为T的缓存数据
     */
    <T> T getData(Class<T> clazz);


    /**
     * 通过cacheId获取缓存数据
     *
     * @param cacheId DataCache对于的Id
     * @return 缓存数据
     */
    Object getData(String cacheId);

    /**
     * 通过DataCache类型获取缓存数据
     *
     * @param clazz DataCache子类
     * @return 缓存数据
     */
    Object getDataOfCache(Class<? extends DataCache> clazz);

}
