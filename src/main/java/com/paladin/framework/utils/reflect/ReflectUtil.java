package com.paladin.framework.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具
 * 
 * @author TontZhou
 * 
 */
public class ReflectUtil {

	/**
	 * 根据属性名称获取对应的get封装方法
	 * <p>
	 * 例如：String name ==> public String getName()
	 * </p>
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public static Method getGetMethod(String name, Class<?> clazz) {

		String methodName = Annotation.class.isAssignableFrom(clazz) ? name : NameUtil.addGet(name);

		Method method = getMethod(clazz, methodName);
		
		// get method 必须满足有返回类型和参数个数为0
		if (method != null && method.getReturnType() != void.class)
			return method;
		
		// is开头的命名规则
		methodName = name.startsWith("is") ? name : NameUtil.addIs(name);

		method = getMethod(clazz, methodName);
		// get method 必须满足有返回类型和参数个数为0
		if (method != null && (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class))
			return method;

		return null;
	}

	/**
	 * 根据属性名称获取对应的set封装方法
	 * <p>
	 * 例如：String name ==> public void setName(String name)
	 * </p>
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public static Method getSetMethod(String name, Class<?> clazz, Class<?> paramType) {
		String methodName = NameUtil.addSet(name);
		Method method = getMethod(clazz, methodName, paramType);
		return method != null ? method : null;
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied
	 * name and no parameters. Searches all superclasses up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 * 
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the method
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method getMethod(Class<?> clazz, String name) {
		return getMethod(clazz, name, new Class<?>[0]);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied
	 * name and parameter types. Searches all superclasses up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 * 
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the method
	 * @param paramTypes
	 *            the parameter types of the method (may be {@code null} to
	 *            indicate any signature)
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * 获取Field（会去父类寻找，包括private，protected）
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Field getField(Class<?> clazz, String name) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			// 这种方式比 getDeclaredField(name) 快
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}

		return null;
	}

	/**
	 * 是否标准GET封装方法
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isGetMethod(Method method) {
		String name = method.getName();
		Class<?> returnType = method.getReturnType();
		return (name.startsWith("get") && !"getClass".equals(name) && name.length() > 3 && method.getParameterTypes().length == 0 && returnType != void.class)
				|| (name.startsWith("is") && name.length() > 2 && method.getParameterTypes().length == 0 && (returnType == boolean.class || returnType == Boolean.class));
	}

	/**
	 * 是否标准SET封装方法
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isSetMethod(Method method) {
		String name = method.getName();
		return name.startsWith("set") && name.length() > 3 && method.getParameterTypes().length == 1;
	}

	/**
	 * 基础类型集合
	 */
	private final static Set<Class<?>> baseClassSet;

	static {
		baseClassSet = new HashSet<>();

		baseClassSet.add(boolean.class);
		baseClassSet.add(char.class);
		baseClassSet.add(byte.class);
		baseClassSet.add(short.class);
		baseClassSet.add(int.class);
		baseClassSet.add(long.class);
		baseClassSet.add(float.class);
		baseClassSet.add(double.class);

		baseClassSet.add(Boolean.class);
		baseClassSet.add(Character.class);
		baseClassSet.add(Byte.class);
		baseClassSet.add(Short.class);
		baseClassSet.add(Integer.class);
		baseClassSet.add(Long.class);
		baseClassSet.add(Float.class);
		baseClassSet.add(Double.class);
		baseClassSet.add(BigDecimal.class);
		baseClassSet.add(BigInteger.class);

		baseClassSet.add(String.class);
		baseClassSet.add(Date.class);

		baseClassSet.add(Object.class);
		baseClassSet.add(Class.class);
	}

	/**
	 * 是否基础类型(基础类型,Class,Object,Number,Enum,Date,String,Character)
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isBaseClass(Class<?> clazz) {
		return baseClassSet.contains(clazz) || clazz.isEnum();

	}

	/**
	 * 获取数组类型
	 * 
	 * @param array
	 *            数组对象类
	 * @return
	 */
	public static Class<?> getArrayType(Class<?> clazz) {
		if (clazz == null || !clazz.isArray())
			return null;
		while (clazz.isArray())
			clazz = clazz.getComponentType();
		return clazz;
	}
	
	/**
	 * 获取泛型实际类型，如果不是泛型或不能识别是否泛型返回NULL
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class<?> getActualTypeArgument(Type type, int index) {

		if (type instanceof ParameterizedType) {
			Type[] ts = ((ParameterizedType) type).getActualTypeArguments();
			return (ts != null && ts.length > index) ? getRawType(ts[index]) : null;
		} else if (type instanceof TypeVariable) {
			Type[] ts = ((TypeVariable) type).getBounds();
			if (ts != null && ts.length > 0)
				return getActualTypeArgument(ts[0], index);
		} else if (type instanceof WildcardType) {
			WildcardType wType = (WildcardType) type;

			// ? extends 的泛型可以限定对象，返回该父类
			Type[] ts = wType.getUpperBounds();
			if (ts.length > 0)
				return getActualTypeArgument(ts[0], index);

			// 使用 ? super 的泛型无法限定对象
		}

		return null;
	}

	/**
	 * 获取原型类型（即Class Type）
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class<?> getRawType(Type type) {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return getRawType(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Class<?> clazz = getRawType(((GenericArrayType) type).getGenericComponentType());
			return Array.newInstance(clazz, 0).getClass();
		} else if (type instanceof TypeVariable) {
			Type[] ts = ((TypeVariable) type).getBounds();
			if (ts != null && ts.length > 0)
				return getRawType(ts[0]);
		} else if (type instanceof WildcardType) {
			WildcardType wType = (WildcardType) type;

			// ? extends 的泛型可以限定对象，返回该父类
			Type[] ts = wType.getUpperBounds();
			if (ts.length > 0)
				return getRawType(ts[0]);
			
			// 使用 ? super 的泛型无法限定对象，这里返回Object.class
			// ts = wType.getLowerBounds();
			// if (ts.length > 0)
			// return getRawType(ts[0]);
			return Object.class;
		}

		return null;
	}

	/**
	 * 获取父类或接口的泛型参数
	 * 
	 * @param targetClass
	 *            目标类
	 * @param superClass
	 *            父类或接口类
	 * @param index
	 *            泛型参数序号 0开始
	 * @return
	 */
	public static Class<?> getSuperClassArgument(Class<?> targetClass, Class<?> superClass, int index) {

		if (index >= 0 && superClass.isAssignableFrom(targetClass) && targetClass != superClass) {
			return _getSuperClassArgument(targetClass, superClass, index);
		}

		return null;
	}

	/**
	 * 获取父类或接口的泛型参数
	 * 
	 * @param targetClass
	 *            目标类
	 * @param superClass
	 *            父类或接口类
	 * @param index
	 *            泛型参数序号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Class<?> _getSuperClassArgument(Class<?> targetClass, Class<?> superClass, int index) {

		Class<?> childClass = getChildClass(superClass, targetClass);
		if (childClass != null) {

			Type type = null;
			if (superClass.isInterface()) {
				Type[] types = childClass.getGenericInterfaces();

				for (Type t : types) {
					if (superClass.equals(getRawType(t))) {
						type = t;
						break;
					}
				}
			} else {
				type = childClass.getGenericSuperclass();
			}

			// 没有标识泛型
			if (type instanceof Class<?>)
				return Object.class;

			if (type instanceof ParameterizedType) {
				// 获取泛型类型
				Type[] types = ((ParameterizedType) type).getActualTypeArguments();
				if (types.length > index) {

					Type argumentType = types[index];
					if(argumentType instanceof Class)
					{
						return (Class<?>) argumentType;
					}
					else if (argumentType instanceof ParameterizedType) {

						// 明确泛型类型直接返回具体类型
						// class A extends B<Integer>

						return (Class<?>) ((ParameterizedType) argumentType).getRawType();
					} else if (argumentType instanceof TypeVariable) {

						/*
						 * 泛型没有明确定义，向下子类寻找是否有明确定义的类型
						 * 
						 * 例如：
						 * 
						 * class B<T> extends A<T> class C extends B<Integer>
						 * 
						 * 在C类中定义了A的泛型类型T为Integer
						 */
						TypeVariable typeVariable = ((TypeVariable) argumentType);

						if (targetClass == childClass) {
							// 如果没有下级子类了，则返回当前可以限定的范围基类
							Type[] ts = typeVariable.getBounds();
							return (Class<?>) (ts.length > 0 ? ts[0] : Object.class);
						} else {

							/*
							 * 根据泛型名称找到对应的子类泛型序号，并且查找子类的子类是否有该泛型类型定义
							 */

							// 查找子类对应的泛型
							TypeVariable[] typeVariables = typeVariable.getGenericDeclaration().getTypeParameters();
							int i = 0;
							for (; i < typeVariables.length; i++) {
								if (typeVariables[i].getName().equals(typeVariable.getName()))
									break;
							}

							// 递归查找子类中是否有对应泛型类型声明
							Class<?> cla = _getSuperClassArgument(targetClass, childClass, i);
							if (cla == null || cla == Object.class) {
								// 如果子类泛型没有声明则会返回Object.class，此时按照当前类型界限返回
								Type[] ts = typeVariable.getBounds();
								return (Class<?>) (ts.length > 0 ? ts[0] : Object.class);
							} else
								return cla;
						}
					} else if (argumentType instanceof GenericArrayType) {

						/*
						 * 数组情况：
						 * 
						 * T[][] 向下子类查找具体泛型类型T Integer[] 直接返回类型 List<T>[]
						 * 返回List，不用管T
						 */
						GenericArrayType genericArrayType = ((GenericArrayType) argumentType);

						Type rootType = genericArrayType.getGenericComponentType();
						int dimension = 1;
						while (rootType instanceof GenericArrayType) {
							rootType = ((GenericArrayType) rootType).getGenericComponentType();
							dimension++;
						}

						Class<?> rootClass = null;
						if (rootType instanceof ParameterizedType) {
							rootClass = (Class<?>) ((ParameterizedType) rootType).getRawType();
						} else if (rootType instanceof TypeVariable) {
							TypeVariable typeVariable = (TypeVariable) rootType;

							if (targetClass == childClass) {
								// 如果没有下级子类了，则返回当前可以限定的范围基类
								Type[] ts = typeVariable.getBounds();
								rootClass = (ts.length > 0 ? getRawType(ts[0]) : Object.class);
							} else {
								// 查找子类对应的泛型
								TypeVariable[] typeVariables = typeVariable.getGenericDeclaration().getTypeParameters();
								int i = 0;
								for (; i < typeVariables.length; i++) {
									if (typeVariables[i].getName().equals(typeVariable.getName()))
										break;
								}

								// 递归查找子类中是否有对应泛型类型声明
								rootClass = _getSuperClassArgument(targetClass, childClass, i);
							}
						}
						return getArrayClass(rootClass, dimension);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 查找指定父类（包括接口）的下一级子类
	 * 
	 * @param superClass
	 * @param targetClass
	 * @return
	 */
	private static Class<?> getChildClass(Class<?> superClass, Class<?> targetClass) {

		if (superClass.isInterface()) {
			if (targetClass == null || targetClass == Object.class)
				return null;

			Class<?>[] clazzs = targetClass.getInterfaces();

			for (Class<?> clazz : clazzs) {
				if (clazz == superClass) {
					return targetClass;
				} else {
					Class<?> cla = getChildClass(superClass, clazz);
					if (cla != null)
						return cla;
				}
			}

			if (!targetClass.isInterface()) {
				Class<?> cla = getChildClass(superClass, targetClass.getSuperclass());
				if (cla != null)
					return cla;
			}
		} else {
			while (targetClass.getSuperclass() != superClass)
				targetClass = targetClass.getSuperclass();
			return targetClass;
		}

		return null;
	}

	/*
	 * 基础类型转化为数组时的名称的编码Map
	 */
	private static final Map<Class<?>, String> primitiveArrayEncodingMap;
	static {
		primitiveArrayEncodingMap = new HashMap<>();
		primitiveArrayEncodingMap.put(boolean.class, "Z");
		primitiveArrayEncodingMap.put(byte.class, "B");
		primitiveArrayEncodingMap.put(char.class, "C");
		primitiveArrayEncodingMap.put(double.class, "D");
		primitiveArrayEncodingMap.put(float.class, "F");
		primitiveArrayEncodingMap.put(int.class, "I");
		primitiveArrayEncodingMap.put(long.class, "J");
		primitiveArrayEncodingMap.put(short.class, "S");
	}

	/**
	 * 获取一定维度的数组类
	 * 
	 * @param clazz
	 * @param dimension
	 * @return
	 */
	private static Class<?> getArrayClass(Class<?> clazz, int dimension) {

		if (dimension > 0) {
			String s = null;
			if (clazz.isArray()) {
				s = clazz.getName();
			} else if (clazz.isPrimitive()) {
				primitiveArrayEncodingMap.get(clazz);
			} else {
				s = "L" + clazz.getName() + ";";
			}

			String className = "[";
			for (--dimension; dimension > 0; dimension--)
				className += "[";

			className += s;
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
			}
		}
		return null;
	}

	/**
	 * 创建一定维度的数组实例
	 * 
	 * @param clazz
	 *            数组基类
	 * @param length
	 *            数组长度
	 * @param dimension
	 *            数组维度 >0
	 * @return
	 */
	public static Object createArray(Class<?> clazz, int length, int dimension) {
		if (dimension > 1)
			clazz = getArrayClass(clazz, --dimension);
		return Array.newInstance(clazz, length);
	}
	
	
	
	private static final Map<Class<?>, Class<?>> primitives = new HashMap<Class<?>, Class<?>>(8);

	static {
		primitives.put(byte.class, Byte.class);
		primitives.put(char.class, Character.class);
		primitives.put(double.class, Double.class);
		primitives.put(float.class, Float.class);
		primitives.put(int.class, Integer.class);
		primitives.put(long.class, Long.class);
		primitives.put(short.class, Short.class);
		primitives.put(boolean.class, Boolean.class);
	}
	
	/**
	 * 获取封装的基础类型
	 * @param primitiveClass
	 * @return
	 */
	public static Class<?> getPackagePrimitive(Class<?> primitiveClass)
	{
		return primitives.get(primitiveClass);
	}
	
	/**
	 * 找到注释有该注解的Field
	 * 
	 * @param clazz
	 * @param annotationClass
	 * @return
	 */
	public static Field[] getAnnotationField(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		List<Field> fieldList = new ArrayList<>();
		_getAnnotationField(clazz, annotationClass, fieldList);
		return fieldList.toArray(new Field[fieldList.size()]);
	}

	private static void _getAnnotationField(Class<?> clazz, Class<? extends Annotation> annotationClass, List<Field> fieldList) {

		if (clazz == null)
			return;

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(annotationClass) != null)
				fieldList.add(field);
		}

		_getAnnotationField(clazz.getSuperclass(), annotationClass, fieldList);
	}

	/**
	 * <ul>
	 * modifier 与修饰符关系
	 * <li>PUBLIC: 1 	1</li>
	 * <li>PRIVATE: 2 	10</li>
	 * <li>PROTECTED: 4 	100</li>
	 * <li>STATIC: 8 	1000</li>
	 * <li>FINAL: 16 	10000</li>
	 * <li>SYNCHRONIZED: 32 	100000</li>
	 * <li>VOLATILE: 64 	1000000</li>
	 * <li>TRANSIENT: 128 	10000000</li>
	 * <li>NATIVE: 256 	100000000</li>
	 * <li>INTERFACE: 512 	1000000000</li>
	 * <li>ABSTRACT: 1024 	10000000000</li>
	 * <li>STRICT: 2048 	100000000000</li>
	 * </ul>
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isStatic(Field field) {
		return (field.getModifiers() & 0x8) != 0;
	}

	
}
