package com.paladin.framework.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paladin.framework.service.annotation.QueryCondition;
import com.paladin.framework.utils.reflect.Entity;
import com.paladin.framework.utils.reflect.EntityField;
import com.paladin.framework.utils.reflect.NameUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * QueryWrapper构建器，类似自带的setEntity功效，这里单独分离出来
 * </p>
 *
 * @author TontZhou
 */
@Slf4j
public class QueryWrapperHelper {

    private static Map<Class<?>, Builder> buildCache = new ConcurrentHashMap<>();

    /**
     * 根据注解构建查询条件
     *
     * @param queryParam
     * @return
     */
    public static QueryWrapper buildQuery(Object queryParam) {
        return buildQuery(new QueryWrapper(), queryParam);
    }

    /**
     * 根据注解构建查询条件（当前criteria，而不是新增，如需要应该在调用前手动调用and()、or()）
     *
     * @param queryWrapper
     * @param queryParam   查询条件参数
     * @return
     */
    public static QueryWrapper buildQuery(QueryWrapper queryWrapper, Object queryParam) {
        if (queryParam == null) {
            return queryWrapper;
        }
        Class<?> clazz = queryParam.getClass();
        if (clazz.isArray()) {
            int length = Array.getLength(queryParam);
            for (int i = 0; i < length; i++) {
                Object param = Array.get(queryParam, i);
                getBuilder(param.getClass()).build(queryWrapper, param);
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection coll = (Collection) queryParam;
            for (Object param : coll) {
                getBuilder(param.getClass()).build(queryWrapper, param);
            }
        } else {
            getBuilder(queryParam.getClass()).build(queryWrapper, queryParam);
        }

        return queryWrapper;
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

    /**
     * 根据QueryWrapper动态SQL规则转化查询条件
     */
    private static void buildCriteria(QueryWrapper queryWrapper, String property, QueryType type, Object value, boolean nullable) {
        if (type == QueryType.EQUAL) {
            if (value == null) {
                if (nullable) {
                    queryWrapper.isNull(true, property);
                }
            } else {
                queryWrapper.eq(true, property, value);
            }
        } else if (type == QueryType.NOT_EQUAL) {
            if (value == null) {
                if (nullable) {
                    queryWrapper.isNotNull(true, property);
                }
            } else {
                queryWrapper.ne(true, property, value);
            }
        } else if (value != null) {
            if (type == QueryType.GREAT_THAN) {
                queryWrapper.gt(true, property, value);
            } else if (type == QueryType.GREAT_EQUAL) {
                queryWrapper.ge(true, property, value);
            } else if (type == QueryType.LESS_THAN) {
                queryWrapper.lt(true, property, value);
            } else if (type == QueryType.LESS_EQUAL) {
                queryWrapper.le(true, property, value);
            } else if (type == QueryType.LIKE) {
                queryWrapper.like(true, property, value);
            } else if (type == QueryType.IN) {
                if (value.getClass().isArray()) {
                    queryWrapper.in(true, property, Arrays.asList(value));
                } else if (value instanceof List) {
                    queryWrapper.in(true, property, (List) value);
                } else {
                    queryWrapper.eq(true, property, value);
                }
            } else if (type == QueryType.IS_NULL) {
                queryWrapper.isNull(true, property);
            } else if (type == QueryType.IS_NOT_NULL) {
                queryWrapper.isNotNull(true, property);
            }
        }
    }


    /**
     * 查询对象构建器
     * <ul>
     * 规则
     * <li>基于{@link SortParam},{@link SortParam}构建排序</li>
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
                        name = NameUtil.hump2underline(entityField.getName());
                    }

                    BuildUnit unit = new BuildUnit(name, condition.type(), entityField.getGetMethod(), condition.nullable());
                    buildUnits.add(unit);
                }
            }
        }

        private QueryWrapper build(QueryWrapper queryWrapper, Object queryParam) {
            if (queryParam instanceof SortParam) {
                SortParam sortParam = (SortParam) queryParam;
                String sort = sortParam.getSort();
                if (sort != null && sort.length() > 0) {
                    sort = NameUtil.hump2underline(sort);
                    String order = sortParam.getOrder();
                    if ("asc".equalsIgnoreCase(order)) {
                        queryWrapper.orderByAsc(sort);
                    } else {
                        queryWrapper.orderByDesc(sort);
                    }
                }
            }

            for (BuildUnit bu : buildUnits) {
                QueryType type = bu.type;
                String property = bu.name;
                try {
                    Object value = bu.getMethod.invoke(queryParam);
                    buildCriteria(queryWrapper, property, type, value, bu.nullable);
                } catch (Exception e) {
                    log.error("build QueryWrapper error!", e);
                    continue;
                }

            }
            return queryWrapper;
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
