package com.paladin.framework.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 获取Spring Bean的工具类，必须在Spring容器启动后才能使用，
 *
 * @author TontZhou
 */
public class SpringBeanHelper implements ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(SpringBeanHelper.class);

    private static ApplicationContext appContext;

    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }

    /**
     * @param name
     * @return 如果异常或找不到则返回null
     */
    public static Object getBean(String name) {
        try {
            return appContext.getBean(name);
        } catch (Exception e) {
            logger.error("获取SpringBean(Name:" + name + ")失败", e);
            return null;
        }
    }

    /**
     * @param name
     * @param requiredType
     * @return 如果异常或找不到则返回null
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        try {
            return appContext.getBean(name, requiredType);
        } catch (Exception e) {
            logger.error("获取SpringBean(Name:" + name + "/Class:" + requiredType.getName() + ")失败", e);
            return null;
        }
    }

    /**
     * 获取某一类的所有bean
     *
     * @param type
     * @return 如果异常或找不到则返回null
     */
    public static <T> Map<String, T> getBeansByType(Class<T> type) {
        try {
            return appContext.getBeansOfType(type);
        } catch (Exception e) {
            logger.error("获取SpringBeansMap(Class:" + type.getName() + ")失败", e);
            return null;
        }
    }

    /**
     * 获取某一类的所有bean
     *
     * @param type                 bean的类型
     * @param includeNonSingletons 是否允许非单例
     * @param allowEagerInit       是否初始化lazy-init的bean
     * @return 如果异常或找不到则返回null
     */
    public static <T> Map<String, T> getBeansByType(Class<T> type, boolean includeNonSingletons,
                                                    boolean allowEagerInit) {
        try {
            return appContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        } catch (Exception e) {
            logger.error("获取SpringBeansMap(Class:" + type.getName() + ")失败", e);
            return null;
        }

    }

    /**
     * 获取某一类的所有bean
     *
     * @param type
     * @return 如果异常或找不到则返回null
     */
    public static <T> T getFirstBeanByType(Class<T> type) {
        try {
            Map<String, T> beansMap = appContext.getBeansOfType(type);
            return getMapFirstValue(beansMap);
        } catch (Exception e) {
            logger.error("获取SpringBean(Class:" + type.getName() + ")失败", e);
            return null;
        }
    }

    /**
     * 获取某一类的所有bean中的第一个
     *
     * @param type                 bean的类型
     * @param includeNonSingletons 是否允许非单例
     * @param allowEagerInit       是否初始化lazy-init的bean
     * @return 如果异常或找不到则返回null
     */
    public static <T> T getFirstBeanByType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) {
        try {
            Map<String, T> beansMap = appContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
            return getMapFirstValue(beansMap);
        } catch (Exception e) {
            logger.error("获取SpringBean(Class:" + type.getName() + ")失败", e);
            return null;
        }

    }

    private static <T> T getMapFirstValue(Map<String, T> map) {
        for (T t : map.values())
            return t;
        return null;
    }
}
