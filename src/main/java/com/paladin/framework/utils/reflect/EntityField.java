package com.paladin.framework.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityField {

	private String name;
	private Field field;
	private Class<?> type;
	private Method getMethod;
	private Method setMethod;

	private Map<Class<? extends Annotation>, Annotation> annotationMap;
	private List<Annotation> annotations;

	EntityField(Field field, Method get, Method set) {
		this.name = field.getName();
		this.field = field;
		this.type = field.getType();
		this.getMethod = get;
		this.setMethod = set;

		annotationMap = new HashMap<>();

		addAnnotation(field.getAnnotations());
		addAnnotation(getMethod.getAnnotations());
		addAnnotation(setMethod.getAnnotations());

		annotations = Collections.unmodifiableList(new ArrayList<>(annotationMap.values()));
	}

	private void addAnnotation(Annotation[] annotations) {
		/*
		 * 只做单注解处理，相同类型注解会被覆盖
		 */
		if (annotations != null) {
			for (Annotation annotation : annotations) {
				annotationMap.put(annotation.annotationType(), annotation);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		return (T) annotationMap.get(type);
	}

	public void setValue(Object source, Object value) {
		try {
			setMethod.invoke(source, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("赋值异常", e);
		}
	}

	public Object getValue(Object source) {
		try {
			return getMethod.invoke(source);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("取值异常", e);
		}
	}

	public boolean isCollection() {
		return Collection.class.isAssignableFrom(type);
	}

	public Class<?> getCollectionType() {
		return ReflectUtil.getActualTypeArgument(field.getGenericType(), 0);
	}
	
	public boolean isMap() {
		return Map.class.isAssignableFrom(type);
	}
	
	public boolean isArray() {
		return type.isArray();
	}
	
	public Class<?> getArrayType() {
		return type.getComponentType();
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public Method getGetMethod() {
		return getMethod;
	}

	public Method getSetMethod() {
		return setMethod;
	}




}
