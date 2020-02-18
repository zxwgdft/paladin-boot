package com.paladin.framework.core.copy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cglib.beans.BeanCopier;
import com.paladin.framework.utils.structure.SecHashMap;

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
	 * @param ignore
	 *            是否忽略某些属性（被注释了{@link IgnoredIfNeed}）
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
	 * @param ignore
	 *            是否忽略某些属性（被注释了{@link IgnoredIfNeed}）
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
	 * @param ignore
	 *            是否忽略某些属性（被注释了{@link IgnoredIfNeed}）
	 * @return
	 */
	public void simpleCopy(Object source, Object target, boolean ignore) {

		if (source == null || target == null) {
			return;
		}

		getCopier(source.getClass(), target.getClass()).copy(source, target, null);
	}

	public static class SimpleBeanCopyUtil {

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
		 * @param ignore
		 *            是否忽略某些属性（被注释了{@link IgnoredIfNeed}）
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
		 * @param ignore
		 *            是否忽略某些属性（被注释了{@link IgnoredIfNeed}）
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
		 * @param ignore
		 *            是否忽略某些属性（被注释了{@link IgnoredIfNeed}）
		 * @return
		 */
		public static void simpleCopy(Object source, Object target, boolean ignore) {
			copier.simpleCopy(source, target, ignore);
		}

	}

}
