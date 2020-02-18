package com.paladin.framework.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {

	private static final Map<Class<?>, Entity> entityMap = new HashMap<>();

	private Class<?> entityClass;
	private String entityName;
	private List<EntityField> entityFields;
	private Map<String, EntityField> fieldMap;

	private Entity(Class<?> entityClass) {
		if (entityClass == null) {
			throw new IllegalArgumentException("实体类不能为NULL");
		}

		if (entityClass.isInterface()) {
			throw new IllegalArgumentException("实体类不能为接口");
		}

		this.entityClass = entityClass;
		this.entityName = entityClass.getSimpleName();

		init();
	}

	private void init() {
		fieldMap = new HashMap<>();

		Class<?> clazz = entityClass;
		while (clazz != null && clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				Class<?> type = field.getType();
				Method getMethod = ReflectUtil.getGetMethod(name, clazz);
				Method setMethod = ReflectUtil.getSetMethod(name, clazz, type);

				if (getMethod != null && setMethod != null) {
					if (!fieldMap.containsKey(name)) {
						fieldMap.put(name, new EntityField(field, getMethod, setMethod));
					}
				}
			}
			clazz = clazz.getSuperclass();
		}

		entityFields = Collections.unmodifiableList(new ArrayList<>(fieldMap.values()));

	}

	public static Entity getEntity(Class<?> clazz) {
		Entity entity = entityMap.get(clazz);
		if (entity == null) {
			synchronized (entityMap) {
				entity = entityMap.get(clazz);
				if (entity == null) {
					entity = new Entity(clazz);
					entityMap.put(clazz, entity);
				}
			}
		}
		return entity;
	}

	public EntityField getEntityField(String name) {
		return fieldMap.get(name);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getEntityName() {
		return entityName;
	}

	public List<EntityField> getEntityFields() {
		return entityFields;
	}

}
