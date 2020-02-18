package com.paladin.framework.utils.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathSetter {

	/**
	 * 根据寻路地址找到相应的值
	 * <p>
	 * 注意，传入sorce不能为空的List或Map，因为将无法判断类型，但是source子属性可以，并且不能有连续数组存在
	 * </p>
	 * 
	 * @param source
	 *            数据源
	 * @param path
	 *            例如address.1.street 表示取address属性下数组的第一个值下的street属性
	 * @param value
	 *            赋值
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void set(Object source, String path, Object value) {

		if (source == null || path == null || "".equals(path))
			return;

		String[] keys = path.split("\\.");

		int len = keys.length;
		int bef = len - 1;

		for (int i = 0; i < bef; i++) {

			if (source == null)
				break;

			String key = keys[i];

			Class<?> clazz = source.getClass();

			if (Map.class.isAssignableFrom(clazz)) {
				Object subObj = ((Map) source).get(key);

				if (subObj == null) {
					Object map = new HashMap<String, Object>();
					((Map) source).put(key, map);
					source = map;
				} else {
					source = subObj;
				}

			} else if (List.class.isAssignableFrom(clazz)) {
				source = ((List) source).get(Integer.valueOf(key));
			} else if (clazz.isArray()) {
				source = Array.get(source, Integer.valueOf(key));
			} else {
				Method getMethod = ReflectUtil.getGetMethod(key, clazz);

				if (getMethod != null) {

					try {
						Object subObj = getMethod.invoke(source);

						if (subObj == null) {

							Object toObj = source;

							Class<?> type = getMethod.getReturnType();

							if (type.isArray()) {

								subObj = Array.newInstance(type, 1);

								if (++i < bef)
									source = getOrCreateArrayItem(subObj, type.getComponentType(), Integer.valueOf(keys[i]));

							} else if (List.class.isAssignableFrom(type)) {

								if (type.isInterface())
									subObj = new ArrayList<>();
								else
									subObj = type.newInstance();
								
								if (++i < bef)
									source = getOrCreateListItem((List) subObj, ReflectUtil.getActualTypeArgument((ParameterizedType) getMethod.getGenericReturnType(), 0), Integer
											.valueOf(keys[i]));
							} else if (Map.class.isAssignableFrom(type)) {
								if (type.isInterface())
									subObj = new HashMap<>();
								else
									subObj = type.newInstance();

								source = subObj;
							} else {
								source = (subObj = type.newInstance());
							}

							Method setMethod = ReflectUtil.getSetMethod(key, clazz, type);
							setMethod.invoke(toObj, subObj);
						} else {
							source = subObj;
						}

					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| InstantiationException e) {

					}

				} else
					throw new RuntimeException("can not find getter for attribution [" + key + "]");
			}
		}

		if (source == null)
			return;

		String key = keys[keys.length - 1];

		if (source instanceof List) {
			List list = (List) source;
			int index = Integer.valueOf(key);
			source = list.remove(index);
			list.add(value);
		} else {
			Class<?> clazz = source.getClass();
			if (clazz.isArray()) {
				int index = Integer.valueOf(key);
				Array.set(source, index, value);
			} else if (source instanceof Map) {
				((Map) source).put(key, value);
			} else {
				setToObject(source, key, value);
			}
		}

	}

	private static Object getOrCreateArrayItem(Object array, Class<?> type, int index) {

		if (type.isArray() || Collection.class.isAssignableFrom(type))
			throw new RuntimeException("do not suppert item of array is array or collection");

		int length = Array.getLength(array);

		if (index >= length) {
			Object s = Array.newInstance(type, index + 1);
			System.arraycopy(array, 0, s, 0, length);
			array = s;
		}

		Object item = Array.get(array, index);
		if (item == null) {
			try {
				item = type.newInstance();
				Array.set(array, index, item);
			} catch (InstantiationException | IllegalAccessException e) {
				return null;
			}
		}

		return item;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object getOrCreateListItem(List list, Class<?> type, int index) {

		if(type == null)
			type = Object.class;
		
		if (type.isArray() || Collection.class.isAssignableFrom(type))
			throw new RuntimeException("do not suppert item of list is array or collection");

		int length = list.size();

		if (index < length)
			return list.get(index);

		if (index > length) {
			for (int i = length; i < index; i++)
				list.add(null);
		}

		try {
			Object item = type.newInstance();
			list.add(item);
			return item;

		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	/**
	 * 
	 * 从Object中通过set方法赋予对应属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setToObject(Object obj, String fieldName, Object value) {

		if (obj != null) {

			Class<?> clazz = obj.getClass();

			try {

				if (value == null) {
					Method[] methods = clazz.getMethods();
					String setName = NameUtil.addSet(fieldName);
					for (Method method : methods) {
						if (method.getName().equals(setName) && method.getParameterTypes().length == 1
								&& method.getReturnType() == void.class)
							method.invoke(obj, new Object[] { null });
					}
				} else {
					Method setMethod = ReflectUtil.getSetMethod(fieldName, clazz, value.getClass());

					if (setMethod == null) {
						return;
					}

					setMethod.invoke(obj, value);
				}
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}

	}
}
