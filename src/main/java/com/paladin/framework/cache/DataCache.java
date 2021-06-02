package com.paladin.framework.cache;

/**
 * 数据缓存
 * <p>
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
public interface DataCache<T> {


    default String getId() {
        return this.getClass().getName();
    }

    /**
     * 加载数据
     *
     * @param version 数据对应版本号
     * @return 返回缓存数据
     */
    T loadData(long version);


}
