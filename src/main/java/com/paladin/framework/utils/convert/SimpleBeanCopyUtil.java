package com.paladin.framework.utils.convert;

import java.util.List;

public class SimpleBeanCopyUtil {

    private static SimpleBeanCopier copier = new SimpleBeanCopier();

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetType
     * @return
     */
    public static <T> List<T> simpleCopyList(List<?> sourceList, Class<T> targetType) {
        return copier.simpleCopyList(sourceList, targetType);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetList
     * @return
     */
    public static <T> List<T> simpleCopyList(List<?> sourceList, List<T> targetList) {
        return copier.simpleCopyList(sourceList, targetList);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetList
     * @param ignore
     * @return
     */
    public static <T> List<T> simpleCopyList(List<?> sourceList, List<T> targetList, boolean ignore) {
        return copier.simpleCopyList(sourceList, targetList, ignore);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetType
     * @param ignore     是否忽略某些属性
     * @return
     */
    public static <T> List<T> simpleCopyList(List<?> sourceList, Class<T> targetType, boolean ignore) {
        return copier.simpleCopyList(sourceList, targetType, ignore);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @return
     */
    public static Object simpleCopy(Object source, Class<?> targetType) {
        return copier.simpleCopy(source, targetType);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @param ignore     是否忽略某些属性
     * @return
     */
    public static Object simpleCopy(Object source, Class<?> targetType, boolean ignore) {
        return copier.simpleCopy(source, targetType, ignore);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @return
     */
    public static void simpleCopy(Object source, Object target) {
        copier.simpleCopy(source, target);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @param ignore 是否忽略某些属性
     * @return
     */
    public static void simpleCopy(Object source, Object target, boolean ignore) {
        copier.simpleCopy(source, target, ignore);
    }
}
