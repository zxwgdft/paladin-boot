package com.paladin.framework.utils.others;

import com.paladin.framework.utils.others.RandomUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 随机对象创建器
 * @author TontoZhou
 *
 */
public class RandomObject {
	
	//随机产生的数组集合等最大长度-1
	private final int array_max_size=3;
	
	/**基础获取值集合*/
	final static Map<Class<?>,RandomValueCreator<?>> baseValueCreatorMap;
	
	static{
		//基础的类对应值创建类集合，优先级低于用户添加的值创建器
		
		baseValueCreatorMap=new HashMap<Class<?>,RandomValueCreator<?>>();
		
		RandomValueCreator<Integer> intCreator=new RandomValueCreator<Integer>(){
			@Override
			public Integer getRandomValue() {
				return RandomUtil.getRandomInt();
			}			
		};
				
		baseValueCreatorMap.put(int.class, intCreator);
		baseValueCreatorMap.put(Integer.class, intCreator);
				
		RandomValueCreator<Double> doubleCreator=new RandomValueCreator<Double>(){
			@Override
			public Double getRandomValue() {
				return RandomUtil.getRandomDouble();
			}			
		};
				
		baseValueCreatorMap.put(double.class, doubleCreator);
		baseValueCreatorMap.put(Double.class, doubleCreator);
				
		RandomValueCreator<Long> longCreator=new RandomValueCreator<Long>(){
			@Override
			public Long getRandomValue() {
				return RandomUtil.getRandomLong();
			}			
		};
				
		baseValueCreatorMap.put(long.class, longCreator);
		baseValueCreatorMap.put(Long.class, longCreator);
		
		RandomValueCreator<Float> floatCreator=new RandomValueCreator<Float>(){
			@Override
			public Float getRandomValue() {
				return RandomUtil.getRandomFloat();
			}			
		};
				
		baseValueCreatorMap.put(float.class, floatCreator);
		baseValueCreatorMap.put(Float.class, floatCreator);
		
		RandomValueCreator<Byte> byteCreator=new RandomValueCreator<Byte>(){
			@Override
			public Byte getRandomValue() {
				return RandomUtil.getRandomByte();
			}			
		};
				
		baseValueCreatorMap.put(byte.class, byteCreator);
		baseValueCreatorMap.put(Byte.class, byteCreator);
			
		RandomValueCreator<Boolean> booleanCreator=new RandomValueCreator<Boolean>(){
			@Override
			public Boolean getRandomValue() {
				return RandomUtil.getRandomBoolean();
			}			
		};
				
		baseValueCreatorMap.put(boolean.class, booleanCreator);
		baseValueCreatorMap.put(Boolean.class, booleanCreator);
		
		baseValueCreatorMap.put(String.class, new RandomValueCreator<String>(){
			@Override
			public String getRandomValue() {
				return RandomUtil.getRandomString();
			}			
		});
		
		baseValueCreatorMap.put(Date.class, new RandomValueCreator<Date>(){
			@Override
			public Date getRandomValue() {
				return new Date();
			}			
		});
		
		baseValueCreatorMap.put(BigDecimal.class, new RandomValueCreator<BigDecimal>(){
			@Override
			public BigDecimal getRandomValue() {
				return new BigDecimal(RandomUtil.getRandomInt());
			}			
		});
	
	}
	
	
	private Map<Class<?>,RandomValueCreator<?>> class2valueMap=new HashMap<Class<?>,RandomValueCreator<?>>();
	
	/**
	 * 添加类(Class)对应值创建器，用于该类(Class)获取值得方式
	 * @param clazz
	 * @param creator
	 */
	public <T> void addClassCreator(Class<T> clazz, RandomValueCreator<T> creator)
	{
		class2valueMap.put(clazz, creator);
	}
	
	private Map<String,RandomValueCreator<?>> field2valueMap=new HashMap<String,RandomValueCreator<?>>();
	
	/**
	 * 添加属性(Field)名称对应值创建器，用于该属性(Field)获取值得方式
	 * @param fieldname
	 * @param creator
	 */
	public <T> void addFieldCreator(String fieldname, RandomValueCreator<T> creator)
	{
		field2valueMap.put(fieldname, creator);
	}
	
	/**
	 * 创建一个对象，并随机赋值，该对象应该是一个简单的只包含属性和属性对应get,set方法的实体类
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T createRandomObject(Class<T> clazz) 
	{
		if(clazz==null)
			return null;
		
		RandomValueCreator<?> creator=class2valueMap.get(clazz);
		if(creator==null)
			creator=baseValueCreatorMap.get(clazz);
		if(creator!=null)
			return (T) creator.getRandomValue();
			
		if(clazz.isInterface())
			return null;
		
		if(clazz==Class.class)	
			return (T) clazz;
		
		if(clazz==Object.class)
			return (T) new Object();
		
		if(clazz.isEnum())
		{
			T[] constants=clazz.getEnumConstants();
			return constants.length>0?constants[RandomUtil.getRandomInt(constants.length-1)]:null;
		}	
		
		if(clazz.isArray())
		{		
			return (T) randomArray(clazz);
		}
		
		T obj;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}
		
		Set<String> fieldNameSet=new HashSet<String>();
		
		Class<?> cla=clazz;
		while(true)
		{
			if(cla==Date.class||cla==Object.class||cla==BigDecimal.class)
				break;
			
			Field[] fields=cla.getDeclaredFields();	
			
			for(Field field:fields)
			{
				String name=field.getName();
				char[] cs=name.toCharArray();
				if(cs[0]>=97&&cs[0]<=122)
				{	
					cs[0]-=32;
					name=String.valueOf(cs);
				}
							
				if(fieldNameSet.contains(name))
					continue;		
			
				Class<?> fieldClass=field.getType();
				try {
					Method setMethod=cla.getMethod("set"+name,fieldClass);	
					Object val=null;
					
					/*
					 * 先从属性对应的值创建器中获取值创建器来得到值，如果未找到，再判断是否为Collection或Map，
					 * 如果是则创建他们（因为无法通过一个class去获取泛型，所以在field这里创建），如果都不是再调
					 * 用随机创建方法createRandomObject()
					 */
					RandomValueCreator<?> rcreator=field2valueMap.get(field.getName());
					if(rcreator!=null)
					{
						val=rcreator.getRandomValue();
					}
					else
					{	
						if(Collection.class.isAssignableFrom(fieldClass))
						{
							Class<?> collClass=(Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
							Collection coll=null;
							if(fieldClass.isInterface())
							{
								//如果是接口,只判断常用的List或Collection，其他接口有需要再添加
								if(fieldClass==List.class||fieldClass==Collection.class)
									coll=new ArrayList();
							}
							else
							{
								//如果不是接口则尝试生成实例
								coll=(Collection) fieldClass.newInstance();									
							}
							
							if(coll!=null)
							{
								int size=RandomUtil.getRandomInt(array_max_size)+1;
								for(int i=0;i<size;i++)
									coll.add(createRandomObject(collClass));
								val=coll;
							}
							
						}
						else if(Map.class.isAssignableFrom(fieldClass))
						{
							Type[] types=((ParameterizedType)field.getGenericType()).getActualTypeArguments();
							Class<?> keyClass=(Class<?>)types [0];
							Class<?> valueClass=(Class<?>)types [1];
							Map map=null;
							if(fieldClass.isInterface())
							{
								//如果是接口，只判断常用的Map时候，创建一个HashMap作为值，如果需要添加其他接口判断，再增加
								if(fieldClass==Map.class)
									map=new HashMap();
							}
							else
							{
								//如果不是接口则尝试生成实例
								map=(Map) fieldClass.newInstance();
							}
							
							if(map!=null)
							{
								//随机赋值
								int size=RandomUtil.getRandomInt(array_max_size)+1;
								for(int i=0;i<size;i++)
									map.put(createRandomObject(keyClass),createRandomObject(valueClass));
								val=map;
							}
							
						}
						else
							val=createRandomObject(fieldClass);
					}
					
					setMethod.invoke(obj, val);					
					fieldNameSet.add(name);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException 
						| IllegalArgumentException | InstantiationException | InvocationTargetException e) {
					e.printStackTrace();
				}			
			}
			
			cla=cla.getSuperclass();
		}
		
		return obj;		
	}
	
	private Object randomArray(Class<?> clazz)
	{
		Class<?> c=clazz.getComponentType();		
		int size=RandomUtil.getRandomInt(array_max_size)+1;
		Object arr=Array.newInstance(c, size);
		for(int i=0;i<size;i++)
		{
			Object val=c.isArray()?randomArray(c):createRandomObject(c);
			Array.set(arr, i, val);
		}
		return arr;
	}
	
	
	public static interface RandomValueCreator<T>{
		T getRandomValue();
	}
}
