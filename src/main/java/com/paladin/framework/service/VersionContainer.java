package com.paladin.framework.service;

/**
 * 版本控制容器接口
 *
 * @author TontoZhou
 * @since 2018年12月12日
 */
public interface VersionContainer {

    /**
     * 容器ID
     *
     * @return
     */
    String getId();

    /**
     * 执行顺序
     *
     * @return
     */
    default int order() {
        return 0;
    }

    ;

    /**
     * 版本改变处理
     *
     * @param version
     * @return
     */
    boolean versionChangedHandle(long version);


}
