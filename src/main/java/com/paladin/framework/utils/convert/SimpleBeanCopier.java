package com.paladin.framework.utils.convert;

import com.paladin.framework.utils.structure.DoubleHashMap;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单BEAN拷贝器
 *
 * @author TontoZhou
 * @since 2018年7月5日
 */
public class SimpleBeanCopier {

    private static DoubleHashMap<Class<?>, Class<?>, BeanCopier> copierMap = new DoubleHashMap<>();

    /**
     * 获取copier，如果没有则创建
     *
     * @param source
     * @param target
     * @return
     */
    private BeanCopier getCopier(Class<?> source, Class<?> target) {
        BeanCopier copier = copierMap.get(source, target);
        if (copier == null) {
            synchronized (copierMap) {
                copier = copierMap.get(source, target);
                if (copier == null) {
                    copier = BeanCopier.create(source, target, false);
                    copierMap.put(source, target, copier);
                }
            }
        }
        return copier;
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetType
     * @return
     */
    public <T> List<T> simpleCopyList(List<?> sourceList, Class<T> targetType) {
        return simpleCopyList(sourceList, targetType, null);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetType
     * @param converter
     * @return
     */
    public <T> List<T> simpleCopyList(List<?> sourceList, Class<T> targetType, Converter converter) {
        if (sourceList == null || targetType == null) {
            return null;
        }
        if (sourceList.size() > 0) {
            Class<?> sourceType = null;
            BeanCopier copier = null;
            List<T> targetList = new ArrayList<>(sourceList.size());
            for (Object source : sourceList) {
                if (source == null) {
                    targetList.add(null);
                    continue;
                }
                if (source.getClass() != sourceType) {
                    sourceType = source.getClass();
                    copier = this.getCopier(sourceType, targetType);
                }
                try {
                    T target = targetType.newInstance();
                    copier.copy(source, target, converter);
                    targetList.add(target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return targetList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetList
     * @return
     */
    public <T> List<T> simpleCopyList(List<?> sourceList, List<T> targetList) {
        return simpleCopyList(sourceList, targetList, null);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetList
     * @param converter
     * @return
     */
    public <T> List<T> simpleCopyList(List<?> sourceList, List<T> targetList, Converter converter) {
        if (sourceList == null || targetList == null) {
            return null;
        }
        int size = Math.min(sourceList.size(), targetList.size());
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                simpleCopy(sourceList.get(i), targetList.get(i), converter);
            }
        }
        return targetList;
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @return
     */
    public <T> T simpleCopy(Object source, Class<T> targetType) {
        return simpleCopy(source, targetType, null);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @param converter
     * @return
     */
    public <T> T simpleCopy(Object source, Class<T> targetType, Converter converter) {
        T target = null;
        try {
            target = targetType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("拷贝失败，创建[" + targetType + "]实例异常", e);
        }
        return simpleCopy(source, target, converter);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @return
     */
    public <T> T simpleCopy(Object source, T target) {
        return simpleCopy(source, target, null);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @param converter
     * @return
     */
    public <T> T simpleCopy(Object source, T target, Converter converter) {
        if (source == null || target == null) {
            return null;
        }
        getCopier(source.getClass(), target.getClass()).copy(source, target, converter);
        return target;
    }


}
