package com.paladin.framework.service;

import com.paladin.framework.utils.reflect.Entity;
import com.paladin.framework.utils.reflect.EntityField;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Criteria构建器
 * </p>
 * <p>
 * 注意：无法创建复杂的查询条件，例如有顺序的OR等，这个方法只是为了大部分情况的简化代码(复杂情况与注解方式扩充待完善)
 * </p>
 *
 * @author TontZhou
 */
public class ExampleHelper {

    private static HashMap<Class<?>, Builder> buildCache = new HashMap<Class<?>, Builder>();


    /**
     * 根据注解构建创建查询条件（当前criteria，而不是新增，如需要应该在调用前手动new一个criteria）
     *
     * @param example
     * @param queryParam 查询条件参数
     * @return
     */
    public static Example buildQuery(Example example, Object queryParam) {
        if (queryParam == null) {
            return example;
        }
        return getBuilder(queryParam.getClass()).build(example, queryParam);
    }

    /**
     * 增加Example的查询条件(new and criteria)
     *
     * @param example
     * @param conditions 查询条件
     * @return
     */
    public static Example buildNewAnd(Example example, Condition... conditions) {
        Criteria criteria = example.createCriteria();
        for (Condition condition : conditions) {
            buildCriteria(criteria, condition);
        }
        example.and(criteria);
        return example;
    }


    /**
     * 增加Example的查询条件(new or criteria)
     *
     * @param example
     * @param conditions 查询条件
     * @return
     */
    public static Example buildNewOr(Example example, Condition... conditions) {
        Criteria criteria = example.createCriteria();
        for (Condition condition : conditions) {
            buildCriteria(criteria, condition);
        }
        example.or(criteria);
        return example;
    }


    /**
     * 增加Example的查询条件(and)
     *
     * @param example
     * @param property  属性名称
     * @param queryType 条件类型
     * @param value     值
     * @return
     */
    public static Example buildNewAnd(Example example, String property, QueryType queryType, Object value) {
        Criteria criteria = example.createCriteria();
        buildCriteria(criteria, property, queryType, value, true);
        example.and(criteria);
        return example;
    }

    /**
     * 增加Example的查询条件(and)
     *
     * @param example
     * @param property  属性名称
     * @param queryType 条件类型
     * @param value     值
     * @return
     */
    public static Example buildNewOr(Example example, String property, QueryType queryType, Object value) {
        Criteria criteria = example.createCriteria();
        buildCriteria(criteria, property, queryType, value, true);
        example.and(criteria);
        return example;
    }


    /**
     * 增加Example的查询条件(在当前criteria下增加条件，如果没有则创建)
     *
     * @param example
     * @param conditions 查询条件
     * @return
     */
    public static Example buildCurrent(Example example, Condition... conditions) {
        Criteria criteria = getCurrentCriteria(example);
        for (Condition condition : conditions) {
            buildCriteria(criteria, condition);
        }
        return example;
    }

    /**
     * 增加Example的查询条件(在当前criteria下增加条件，如果没有则创建)
     *
     * @param example
     * @param property  属性名称
     * @param queryType 条件类型
     * @param value     值
     * @return
     */
    public static Example buildCurrent(Example example, String property, QueryType queryType, Object value) {
        Criteria criteria = getCurrentCriteria(example);
        buildCriteria(criteria, property, queryType, value, true);
        return example;
    }

    /**
     * 获取当前criteria，如果没有则创建
     *
     * @param example
     * @return
     */
    private static Criteria getCurrentCriteria(Example example) {
        List<Criteria> oredCriteria = example.getOredCriteria();
        int size = oredCriteria.size();
        if (size == 0) {
            return example.createCriteria();
        } else if (size == 1) {
            return oredCriteria.get(0);
        } else {
            return oredCriteria.get(size - 1);
        }
    }

    /**
     * 获取查询条件构建起
     */
    private static Builder getBuilder(Class<?> queryParamClass) {
        Builder builder = buildCache.get(queryParamClass);
        if (builder == null) {
            // 同步创建Builder
            synchronized (buildCache) {
                builder = buildCache.get(queryParamClass);
                if (builder == null) {
                    builder = new Builder(queryParamClass);
                }
            }
        }
        return builder;
    }

    private static void buildCriteria(Criteria criteria, Condition condition) {
        buildCriteria(criteria, condition.name, condition.type, condition.value, true);
    }

    /**
     * 根据Example动态SQL规则转化查询条件
     */
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
     * 查询对象构建器
     * <ul>
     * 规则
     * <li>基于{@link QuerySort},{@link QuerySorts}构建排序</li>
     * <li>基于{@link QueryCondition}构建简单查询条件（AND）</li>
     * <li></li>
     * <li></li>
     * </ul>
     *
     * @author TontoZhou
     * @since 2018年3月15日
     */
    private static class Builder {

        private ArrayList<BuildUnit> buildUnits;

        private Builder(Class<?> queryClass) {
            this.buildUnits = new ArrayList<BuildUnit>();
            for (EntityField entityField : Entity.getEntity(queryClass).getEntityFields()) {
                QueryCondition condition = entityField.getAnnotation(QueryCondition.class);
                if (condition != null) {
                    String name = condition.name();

                    // 默认使用方法对应的field名作为column
                    if ("".equals(name)) {
                        name = entityField.getName();
                    }

                    BuildUnit unit = new BuildUnit(name, condition.type(), entityField.getGetMethod(), condition.nullable());
                    buildUnits.add(unit);
                }
            }
        }

        private Example build(Example example, Object queryParam) {
            boolean hasSort = false;
            if (queryParam instanceof QuerySort) {
                QuerySort sortQuery = (QuerySort) queryParam;
                String sort = sortQuery.getSort();
                if (sort != null && sort.length() > 0) {
                    String order = sortQuery.getOrder();
                    hasSort = true;
                    if ("asc".equalsIgnoreCase(order)) {
                        example.orderBy(sort).asc();
                    } else {
                        example.orderBy(sort).desc();
                    }
                }
            } else if (queryParam instanceof QuerySorts) {
                QuerySorts querySorts = (QuerySorts) queryParam;
                String[] orders = querySorts.getOrder();
                String[] sorts = querySorts.getSort();
                for (int i = 0; orders.length > i && sorts.length > i; i++) {
                    String order = orders[i];
                    String sort = sorts[i];
                    if (sort != null && sort.length() > 0) {
                        hasSort = true;
                        if ("asc".equalsIgnoreCase(order)) {
                            example.orderBy(sort).asc();
                        } else {
                            example.orderBy(sort).desc();
                        }
                    }
                }
            }

            if (buildUnits.size() == 0 && !hasSort) {
                return null;
            }

            Criteria criteria = getCurrentCriteria(example);
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

        private BuildUnit(String name, QueryType type, Method getMethod, boolean nullable) {
            this.nullable = nullable;
            this.name = name;
            this.type = type;
            this.getMethod = getMethod;
        }
    }


}
