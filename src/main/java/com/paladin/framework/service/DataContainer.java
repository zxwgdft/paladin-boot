package com.paladin.framework.service;

/**
 * 数据容器接口
 *
 * @author TontoZhou
 * @since 2018年12月12日
 */
public interface DataContainer {

    default String getId() {
        return this.getClass().getName();
    }

    void load();

    default void reload() {
        load();
    }

    default int order() {
        return 0;
    }
}
