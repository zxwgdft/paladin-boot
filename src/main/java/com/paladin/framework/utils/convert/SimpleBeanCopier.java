package com.paladin.framework.utils.convert;

import com.paladin.framework.utils.structure.SecHashMap;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单BEAN拷贝器
 *
 * @author TontoZhou
 * @since 2018年7月5日
 */
public class SimpleBeanCopier {

    private static SecHashMap<Class<?>, Class<?>, BeanCopier> copierMap = new SecHashMap<>();

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
        return simpleCopyList(sourceList, targetType, false);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetType
     * @param ignore     是否忽略某些属性
     * @return
     */
    public <T> List<T> simpleCopyList(List<?> sourceList, Class<T> targetType, boolean ignore) {
        if (sourceList.size() > 0) {

            Class<?> sourceType = sourceList.get(0).getClass();
            BeanCopier copier = getCopier(sourceType, targetType);

            // 这里为了效率，暂时视为所有集合对象都是一个类

            List<T> targetList = new ArrayList<>(sourceList.size());
            for (Object source : sourceList) {
                try {
                    T target = targetType.newInstance();
                    copier.copy(source, target, null);
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
        return simpleCopyList(sourceList, targetList, false);
    }

    /**
     * 简单拷贝多个对象
     *
     * @param sourceList
     * @param targetList
     * @param ignore
     * @return
     */
    public <T> List<T> simpleCopyList(List<?> sourceList, List<T> targetList, boolean ignore) {
        int size = Math.min(sourceList.size(), targetList.size());
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                simpleCopy(sourceList.get(i), targetList.get(i), ignore);
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
    public Object simpleCopy(Object source, Class<?> targetType) {
        return simpleCopy(source, targetType, false);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param targetType
     * @param ignore     是否忽略某些属性
     * @return
     */
    public Object simpleCopy(Object source, Class<?> targetType, boolean ignore) {

        Object target = null;
        try {
            target = targetType.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        simpleCopy(source, target, ignore);
        return target;
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @return
     */
    public void simpleCopy(Object source, Object target) {
        simpleCopy(source, target, false);
    }

    /**
     * 简单拷贝对象
     *
     * @param source
     * @param target
     * @param ignore 是否忽略某些属性
     * @return
     */
    public void simpleCopy(Object source, Object target, boolean ignore) {

        if (source == null || target == null) {
            return;
        }

        getCopier(source.getClass(), target.getClass()).copy(source, target, null);
    }


}
