package com.paladin.data.struct;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.paladin.framework.utils.reflect.ReflectUtil;

public class ObjectContainer<K, T> {

	private Class<T> valueClass;

	private final static Map<Class<?>, Class<?>> cacheMap = new HashMap<>();

	@SuppressWarnings("unchecked")
	public ObjectContainer() {

		Class<?> clazz = this.getClass();
		Class<?> valueClass = cacheMap.get(clazz);

		if (valueClass == null) {
			synchronized (cacheMap) {
				valueClass = cacheMap.get(clazz);
				if (valueClass == null) {
					valueClass = ReflectUtil.getSuperClassArgument(clazz, ObjectContainer.class, 1);
					cacheMap.put(clazz, valueClass);
				}
			}
		}

		this.valueClass = (Class<T>) valueClass;
	}

	Map<K, T> objectMap = new ConcurrentHashMap<>();

	public T getChild(K key) {
		return objectMap.get(key);
	}

	private volatile T[] cacheArray = null;
	private AtomicBoolean changed = new AtomicBoolean(false);
	
	@SuppressWarnings("unchecked")
	public T[] getChildren() {
		
		if(changed.getAndSet(false) || cacheArray == null)
		{
			Collection<T> coll = objectMap.values();
			cacheArray = sort(coll.toArray((T[]) Array.newInstance(valueClass, coll.size())));	
		}
		
		return cacheArray;
	}

	public void addChild(K key, T value) {
		objectMap.put(key, value);
		changed.set(true);
	}

	public T removeChild(K key) {
		T t = objectMap.remove(key);
		changed.set(true);
		return t;
	}

	protected T[] sort(T[] array){
		return array;
	}
	
}
