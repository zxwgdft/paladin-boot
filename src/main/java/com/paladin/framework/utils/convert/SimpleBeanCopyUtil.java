package com.paladin.framework.utils.convert;

import org.springframework.cglib.core.Converter;

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
     * @param converter
     * @return
     */
    public static <T> List<T> simpleCopyList(List<?> sourceList, List<T> targetList, Converter converter) {
        return copier.simpleCopyList(sourceList, targetList, converter);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetType
     * @param converter
     * @return
     */
    public static <T> List<T> simpleCopyList(List<?> sourceList, Class<T> targetType, Converter converter) {
        return copier.simpleCopyList(sourceList, targetType, converter);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @return
     */
    public static <T> T simpleCopy(Object source, Class<T> targetType) {
        return copier.simpleCopy(source, targetType);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @param converter
     * @return
     */
    public static <T> T simpleCopy(Object source, Class<T> targetType, Converter converter) {
        return copier.simpleCopy(source, targetType, converter);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @return
     */
    public static <T> T simpleCopy(Object source, T target) {
        return copier.simpleCopy(source, target);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @param converter
     * @return
     */
    public static <T> T simpleCopy(Object source, T target, Converter converter) {
        return copier.simpleCopy(source, target, converter);
    }
}
