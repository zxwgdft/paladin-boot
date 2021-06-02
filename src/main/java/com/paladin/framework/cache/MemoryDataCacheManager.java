package com.paladin.framework.cache;

import lombok.extern.slf4j.Slf4j;

/**
 * 内存型数据缓存
 *
 * @author TontoZhou
 * @since 2021/3/19
 */
@Slf4j
public class MemoryDataCacheManager extends AbstractDataCacheManager {

    @Override
    protected DataCacheWrapper getDataCacheWrapper(DataCache source) {
        return new MemoryDataCacheWrapper(source);
    }

}
