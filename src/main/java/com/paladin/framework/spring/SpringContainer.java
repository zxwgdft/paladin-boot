package com.paladin.framework.spring;

/**
 * Spring 框架下的容器，在Spring上下文启动完毕后启动这些容器
 *
 * @author TontZhou
 */
public interface SpringContainer {

    /**
     * 项目启动完毕后运行
     *
     * @return
     */
    default boolean initialize() {
        return true;
    }

    ;

    /**
     * 所有{@link SpringContainer}的实例的{@code initialize()}方法执行后执行
     *
     * @return
     */
    default boolean afterInitialize() {
        return true;
    }

    ;

    /**
     * 执行顺序
     *
     * @return
     */
    default int order() {
        return 0;
    }

}
