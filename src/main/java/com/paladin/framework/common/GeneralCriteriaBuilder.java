package com.paladin.framework.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * <p>
 * Criteria构建器
 * </p>
 * <p>
 * 注意：无法创建复杂的查询条件，例如有顺序的OR等，这个方法只是为了大部分情况的简化代码(复杂情况与注解方式扩充待完善)
 * </p>
 * 
 * @author TontZhou
 *
 */
public class GeneralCriteriaBuilder {

	private static HashMap<Class<?>, Builder> buildCache = new HashMap<Class<?>, Builder>();

	private static ThreadLocal<HashMap<Class<?>, Example>> local_example = new ThreadLocal<HashMap<Class<?>, Example>>() {
		public HashMap<Class<?>, Example> initialValue() {
			return new HashMap<Class<?>, Example>();
		}
	};

	/**
	 * 获取干净的基于线程变量的Example实例
	 * 
	 * @param entityClass
	 * @return
	 */
	public static Example getClearCurrentExample(Class<?> entityClass) {
		Example example = getCurrentExample(entityClass);
		example.clear();
		return example;
	}

	/**
	 * 获取当前线程Example
	 * <p>
	 * 注意：基于该类的大多数方法都使用了线程变量的Example，除非想使用Example来实现复杂查询，否则尽量使用本类提供的其他方法
	 * </p>
	 * 
	 * @param entityClass
	 * @return
	 */
	public static Example getCurrentExample(Class<?> entityClass) {
		HashMap<Class<?>, Example> map = local_example.get();
		Example example = map.get(entityClass);
		if (example == null) {
			synchronized (local_example) {
				if (example == null) {
					example = new Example(entityClass);
					map.put(entityClass, example);
				}
			}
		}
		return example;
	}

	/**
	 * 根据注解构建一个查询的example，可查看{@link Builder}
	 * 
	 * 
	 * @param clazz
	 * @param queryParam
	 * @return
	 */
	public static Example buildQuery(Class<?> entityType, Object queryParam) {
		if (queryParam == null)
			return null;
		return getBuilder(entityType, queryParam).build(queryParam);
	}

	/**
	 * 增加Example的查询条件
	 * 
	 * @param example
	 * @param conditions
	 * @return
	 */
	public static Example buildAnd(Example example, List<Condition> conditions) {

		List<Criteria> criterias = example.getOredCriteria();
		Criteria criteria = null;

		if (criterias.size() == 0) {
			criteria = example.createCriteria();
		} else {
			criteria = criterias.get(criterias.size() - 1);
		}

		for (Condition condition : conditions)
			buildCriteria(criteria, condition);

		return example;
	}

	/**
	 * 增加Example的查询条件
	 * 
	 * @param example
	 * @param property
	 * @param queryType
	 * @param value
	 * @param value2
	 * @return
	 */
	public static Example buildAnd(Example example, String property, QueryType queryType, Object value) {

		List<Criteria> criterias = example.getOredCriteria();
		Criteria criteria = null;

		if (criterias.size() == 0) {
			criteria = example.createCriteria();
		} else {
			criteria = criterias.get(criterias.size() - 1);
		}

		buildCriteria(criteria, property, queryType, value, true);
		return example;
	}

	/**
	 * 注意：该Example为线程变量，会清空Example后填入查询条件，应当作为创建Example来用
	 * 
	 * @param entityType
	 * @param conditions
	 * @return
	 */
	public static Example buildAnd(Class<?> entityType, List<Condition> conditions) {
		Example example = getClearCurrentExample(entityType);
		Criteria criteria = example.createCriteria();
		for (Condition condition : conditions)
			buildCriteria(criteria, condition);
		return example;
	}

	/**
	 * 注意：该Example为线程变量，会清空Example后填入查询条件，应当作为创建Example来用
	 * 
	 * @param entityType
	 * @param conditions
	 * @return
	 */
	public static Example buildAnd(Class<?> entityType, Condition condition) {
		Example example = getClearCurrentExample(entityType);
		Criteria criteria = example.createCriteria();
		buildCriteria(criteria, condition);
		return example;
	}

	/**
	 * 注意：该Example为线程变量，会清空Example后填入查询条件，应当作为创建Example来用
	 * 
	 * @param entityType
	 * @param property
	 * @param queryType
	 * @param value
	 * @param value2
	 * @return
	 */
	public static Example buildAnd(Class<?> entityType, String property, QueryType queryType, Object value) {
		Example example = getClearCurrentExample(entityType);
		Criteria criteria = example.createCriteria();
		buildCriteria(criteria, property, queryType, value, true);
		return example;
	}

	/**
	 * 获取查询条件构建起
	 * 
	 * @param entityType
	 * @param queryParam
	 * @return
	 */
	private static Builder getBuilder(Class<?> entityType, Object queryParam) {
		Class<?> clazz = queryParam.getClass();
		Builder builder = buildCache.get(clazz);
		if (builder == null) {
			// 同步创建Builder
			synchronized (buildCache) {
				builder = buildCache.get(clazz);
				if (builder == null) {

					// 根据get方法上的QueryCondition注解作为构建单元
					builder = new Builder(entityType);
					Method[] methods = clazz.getMethods();

					for (Method method : methods) {
						QueryCondition condition = method.getAnnotation(QueryCondition.class);
						if (condition != null && ReflectUtil.isGetMethod(method)) {
							String name = condition.name();

							// 默认使用方法对应的field名作为column
							if ("".equals(name))
								name = NameUtil.removeGetOrSet(method.getName());
							
							Class<?> entityClass = condition.entityClass();	
							if(entityClass == Object.class) {
								entityClass = entityType;
							}
							BuildUnit unit = new BuildUnit(name, entityClass, condition.type(), method, condition.nullable());
							builder.buildUnits.add(unit);
						}
					}
				}
			}
		}

		return builder;

	}

	/**
	 * 查询对象构建器
	 * <ul>
	 * 规则
	 * <li>基于{@link QuerySort}构建排序</li>
	 * <li>基于{@link QueryCondition}构建简单查询条件（AND）</li>
	 * <li></li>
	 * <li></li>
	 * </ul>
	 * 
	 * @author TontoZhou
	 * @since 2018年3月15日
	 */
	private static class Builder {

		private Class<?> entityClass;

		private Builder(Class<?> entityClass) {
			this.entityClass = entityClass;
		}

		private ArrayList<BuildUnit> buildUnits = new ArrayList<BuildUnit>();

		private Example build(Object queryParam) {

			Example example = getClearCurrentExample(entityClass);

			boolean hasSort = false;
			if (queryParam instanceof QuerySort) {

				QuerySort sortQuery = (QuerySort) queryParam;
				String sort = sortQuery.getSort();
				if (sort != null) {
					String order = sortQuery.getOrder();
					if ("asc".equals(order)) {
						example.orderBy(sort).asc();
					} else {
						example.orderBy(sort).desc();
					}
				}

				hasSort = true;
			} else if (queryParam instanceof QuerySorts) {

				QuerySorts sortQuerys = (QuerySorts) queryParam;
				String[] orders = sortQuerys.getOrder();
				String[] sorts = sortQuerys.getSort();

				for (int i = 0; orders.length > i && sorts.length > i; i++) {
					String order = orders[i];
					String sort = sorts[i];
					if ("asc".equals(order)) {
						example.orderBy(sort).asc();
					} else {
						example.orderBy(sort).desc();
					}
					hasSort = true;
				}
			}

			if (buildUnits.size() == 0 && !hasSort) {
				return example;
			}

			Criteria criteria = example.createCriteria();

			for (BuildUnit bu : buildUnits) {
				QueryType type = bu.type;
				String property = bu.name;
				try {
					Object value = bu.getMethod.invoke(queryParam);
					buildCriteria(criteria, property, type, value, bu.nullable);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}

			return example;
		}

	}

	private static void buildCriteria(Criteria criteria, Condition condition) {
		buildCriteria(criteria, condition.name, condition.type, condition.value, true);
	}

	/**
	 * 根据Example动态SQL规则转化查询条件
	 * 
	 * @param criteria
	 * @param property
	 * @param type
	 * @param value
	 * @param nullable
	 */
	@SuppressWarnings({ "rawtypes" })
	private static void buildCriteria(Criteria criteria, String property, QueryType type, Object value, boolean nullable) {

		if (type == QueryType.EQUAL) {
			if (value == null) {
				if (nullable) {
					criteria.andIsNull(property);
				}
			} else {
				criteria.andEqualTo(property, value);
			}
		} else if (type == QueryType.NOT_EQUAL) {
			criteria.andNotEqualTo(property, value);
		} else if (type == QueryType.IS_NULL) {
			criteria.andIsNull(property);
		} else if (type == QueryType.IS_NOT_NULL) {
			criteria.andIsNotNull(property);
		} else if (value != null) {
			if (type == QueryType.GREAT_THAN) {
				criteria.andGreaterThan(property, value);
			} else if (type == QueryType.GREAT_EQUAL) {
				criteria.andGreaterThanOrEqualTo(property, value);
			} else if (type == QueryType.LESS_THAN) {
				criteria.andLessThan(property, value);
			} else if (type == QueryType.LESS_EQUAL) {
				criteria.andLessThanOrEqualTo(property, value);
			} else if (type == QueryType.LIKE) {
				criteria.andLike(property, "%" + value + "%");
			} else if (type == QueryType.IN) {
				if (value.getClass().isArray()) {
					criteria.andIn(property, Arrays.asList(value));
				} else if (value instanceof List) {
					criteria.andIn(property, (List) value);
				} else {
					criteria.andIn(property, Arrays.asList(value));
				}
			}
		}
	}

	/**
	 * 构建单元
	 * 
	 * @author TontoZhou
	 * @since 2018年3月15日
	 */
	private static class BuildUnit {

		private QueryType type;
		private String name;
		private Method getMethod;
		private boolean nullable;
		// 用于连接查询，暂时搁置
		@SuppressWarnings("unused")
		private Class<?> entityClass;

		private BuildUnit(String name, Class<?> entityClass, QueryType type, Method getMethod, boolean nullable) {
			this.nullable = nullable;
			this.name = name;
			this.type = type;
			this.getMethod = getMethod;
			this.entityClass = entityClass;
		}

	}


}
