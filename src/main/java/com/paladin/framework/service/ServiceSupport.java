package com.paladin.framework.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.paladin.framework.common.BaseModel;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.exception.SystemException;
import com.paladin.framework.exception.SystemExceptionCode;
import com.paladin.framework.mybatis.CustomMapper;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import com.paladin.framework.utils.convert.SimpleConvertUtil;
import com.paladin.framework.utils.reflect.Entity;
import com.paladin.framework.utils.reflect.EntityField;
import com.paladin.framework.utils.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.OrderBy;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <h2>业务支持类</h2>
 * 提供一些简单业务方法
 * 通过{@link ServiceSupportConatiner}自动注册sqlMapper
 * 简单模式下会忽略通用筛查、排序条件
 *
 * @param <Model> 实体类
 * @author TontoZhou
 */
@Slf4j
public abstract class ServiceSupport<Model> {

    /**
     * 选择项缓存
     */
    private static Map<Class, String[]> selectionCacheMap = new HashMap<>();


    protected Class<Model> modelType; // 业务对应类

    public ServiceSupport() {
        // 获取泛型类，该泛型类应该是对应数据库某表的实体类类型
        Class<?> clazz = ReflectUtil.getSuperClassArgument(this.getClass(), ServiceSupport.class, 0);
        if (clazz == null) {
            log.warn("[" + this.getClass().getName() + "]的实现类没有明确定义[" + ServiceSupport.class.getName() + "]的泛型，无法为其注册SqlMapper");
        }
        modelType = (Class<Model>) clazz;
    }

    // -------------------------- 初始化配置 --------------------------


    protected Condition[] commonConditions; // 通用查询条件
    protected boolean isBaseModel = false; // 是否基于基础模型

    protected String[] ignoreSelections; // 列表查询时忽略字段（例如某些大文本，并不需要在列表查询中返回）

    /*
     * 排序属性
     */
    protected String[] orderByProperties;
    protected OrderType[] orderByTypes;
    protected boolean hasOrderBy = false;
    protected boolean isAlwaysOrderFirst = false;

    // 主键方法
    private Method[] pkGetMethods; // 主键对应get方法
    private Method[] pkSetMethods; // 主键对应set方法
    private final Object lock = new Object();

    /**
     * 初始化
     */
    public void init() {
        if (BaseModel.class.isAssignableFrom(modelType)) {
            isBaseModel = true;
        }

        Class<?> type = this.getClass();

        /*
         * 初始化通用排序
         */
        CommonOrderBy orderBy = type.getAnnotation(CommonOrderBy.class);
        if (orderBy != null) {
            isAlwaysOrderFirst = orderBy.orderByFirst();
            int size = Math.min(orderBy.property().length, orderBy.type().length);
            if (size > 0) {
                orderByProperties = new String[size];
                orderByTypes = new OrderType[size];
                for (int i = 0; i < size; i++) {
                    orderByProperties[i] = orderBy.property()[i];
                    orderByTypes[i] = orderBy.type()[i];
                }
                hasOrderBy = true;
            }
        }

        /*
         * 如果没有排序规则，但是是基础对象则默认倒叙创建时间
         */
        if (!hasOrderBy && isBaseModel) {
            orderByProperties = new String[]{BaseModel.FIELD_CREATE_TIME};
            orderByTypes = new OrderType[]{OrderType.DESC};
            hasOrderBy = true;
        }

        // 通用查询条件
        List<Condition> commonConditionList = new ArrayList<>();

        CommonCondition commonCondition = type.getAnnotation(CommonCondition.class);
        CommonConditions commonConditions = type.getAnnotation(CommonConditions.class);

        if (commonCondition != null) {
            commonConditionList.add(convert2Condition(commonCondition));
        }

        if (commonConditions != null) {
            for (CommonCondition cc : commonConditions.value()) {
                commonConditionList.add(convert2Condition(cc));
            }
        }

        // 如果是逻辑删除模型，则所有查询中需要过滤删除数据
        if (isBaseModel) {
            commonConditionList.add(new Condition(BaseModel.FIELD_DELETED, QueryType.EQUAL, BaseModel.BOOLEAN_NO));
        }

        this.commonConditions = commonConditionList.toArray(new Condition[commonConditionList.size()]);

        // 获取主键get,set方法
        if (pkGetMethods == null) {
            synchronized (lock) {
                if (pkGetMethods == null) {
                    // 判断是否存在主键
                    Set<EntityColumn> pkColumns = EntityHelper.getEntityTable(modelType).getEntityClassPKColumns();
                    if (pkColumns == null || pkColumns.size() == 0) {
                        pkGetMethods = new Method[0];
                    } else {
                        pkGetMethods = new Method[pkColumns.size()];
                        pkSetMethods = new Method[pkColumns.size()];
                        int i = 0;
                        for (EntityColumn column : pkColumns) {
                            String property = column.getProperty();
                            EntityField entityField = Entity.getEntity(modelType).getEntityField(property);
                            pkGetMethods[i] = entityField.getGetMethod();
                            pkSetMethods[i++] = entityField.getSetMethod();
                        }
                    }
                }
            }
        }


        // 获取忽略查询项
        List<String> ignoreSelectionList = new ArrayList<>();
        for (EntityField entityField : Entity.getEntity(modelType).getEntityFields()) {
            if (entityField.getAnnotation(IgnoreSelection.class) != null) {
                ignoreSelectionList.add(entityField.getName());
            }
        }

        ignoreSelections = ignoreSelectionList.toArray(new String[ignoreSelectionList.size()]);
    }


    /**
     * 转换通用查询条件注解
     *
     * @param commonCondition 通用查询条件注解
     * @return 返回通用查询条件
     */
    protected Condition convert2Condition(CommonCondition commonCondition) {
        String v1 = commonCondition.value();
        Class<?> type = commonCondition.valueType();
        String name = commonCondition.name();
        QueryType queryType = commonCondition.type();

        Object o1 = null;

        if (!"".equals(v1)) {
            // IN 查询，value值应该为逗号分隔的字符串，例如 1,2,3,4
            if (queryType == QueryType.IN) {
                String[] vs = v1.split(",");
                ArrayList<Object> list = new ArrayList<>(vs.length);
                for (String v : vs) {
                    list.add(SimpleConvertUtil.parseString(v, type));
                }
                o1 = list;
            } else {
                o1 = SimpleConvertUtil.parseString(v1, type);
            }
        }
        return new Condition(name, queryType, o1);
    }


    /**
     * 获取主键
     *
     * @param model 实体类 实体类
     * @return 主键值，如果是多个主键将返回数组
     */
    protected Object getPKValue(Model model) {
        if (model == null || pkGetMethods.length == 0) {
            return null;
        }

        if (pkGetMethods.length == 1) {
            try {
                return pkGetMethods[0].invoke(model);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("无法调用主键相应GET方法：" + pkGetMethods[0].getName(), e);
            }
        } else {
            Object[] pkObjects = new Object[pkGetMethods.length];
            for (int i = 0; i < pkGetMethods.length; i++) {
                Method method = pkGetMethods[i];
                try {
                    pkObjects[i] = method.invoke(model);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    log.error("无法调用主键相应GET方法：" + method.getName(), e);
                }
            }
            return pkObjects;
        }
        return null;
    }

    /**
     * 判断是否存在主键
     * 多主键下，只要一个主键存在空则算为空主键
     *
     * @param model 实体类
     * @return
     */
    protected boolean judgeHasPKValue(Model model) {
        if (model == null || pkGetMethods.length == 0) {
            return false;
        }

        for (Method method : pkGetMethods) {
            try {
                Object value = method.invoke(model);
                if (value == null || "".equals(value)) {
                    return false;
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("无法调用主键相应GET方法：" + method.getName(), e);
            }
        }

        return true;
    }

    /**
     * 检查主键是否是空字符串，如果是则置为null
     *
     * @param model 实体类
     */
    protected void checkEmptyId(Model model) {
        if (model == null || pkGetMethods.length == 0) {
            return;
        }

        for (int i = 0; i < pkGetMethods.length; i++) {
            Method getMethod = pkGetMethods[i];
            try {
                if ("".equals(getMethod.invoke(model))) {
                    pkSetMethods[i].invoke(model, null);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("无法调用主键相应GET或SET方法：" + getMethod.getName() + "/" + pkSetMethods[i].getName(), e);
            }
        }
    }

    // -----------------------------------------------------
    // 通用业务方法
    // -----------------------------------------------------

    private CustomMapper<Model> customMapper;

    public CustomMapper<Model> getSqlMapper() {
        return customMapper;
    }

    public void setSqlMapper(CustomMapper<Model> customMapper) {
        this.customMapper = customMapper;
    }

    /**
     * 根据主键获取对象
     *
     * @param pk 主键
     * @return
     */
    public Model get(Object pk) {
        return getSqlMapper().selectByPrimaryKey(pk);
    }

    /**
     * 根据主键获取对象
     *
     * @param pk      主键
     * @param VOClass view object class
     * @return
     */
    public <T> T get(Object pk, Class<T> VOClass) {
        Model model = getSqlMapper().selectByPrimaryKey(pk);
        if (model != null) {
            if (VOClass != modelType) {
                return SimpleBeanCopyUtil.simpleCopy(model, VOClass);
            }
        }
        return (T) model;
    }

    // -----------------------------------------------------
    // 查询，包含权限判断
    // -----------------------------------------------------

    /**
     * 获取Example（可以考虑线程变量，但是好像意义不大，并且可能出现查询冲突）
     *
     * @return new Example
     */
    public Example getExample() {
        return new Example(modelType);
    }


    /**
     * 构建通用部分，该部分条件添加在最后一组criteria，
     * 在存在多个criteria,并且有or条件时会出现不正确结果，
     * 如果有这种复查查询SQL，请改为xml中写sql形式，或手动自行调整
     *
     * @param example 查询标准集
     * @return
     */
    public Example buildCommon(Example example) {
        if (commonConditions.length > 0 || hasOrderBy) {
            if (example == null) {
                example = getExample();
            }
            if (commonConditions.length > 0) {
                ExampleHelper.buildCurrent(example, commonConditions);
            }
            if (hasOrderBy) {
                buildCommonOrderBy(example);
            }
        }
        return example;
    }

    /**
     * 构建通用排序
     * 如果isAlwaysOrderFirst = true，通用部分排序将调整到最前
     * 该方法应该总是在Example构建排序的最后，避免排序位置错误
     *
     * @param example 查询标准集
     * @return
     */
    public Example buildCommonOrderBy(Example example) {
        if (hasOrderBy) {
            if (example == null) {
                example = getExample();
            }

            String orderByClause = null;
            boolean hasFirst = false;

            if (isAlwaysOrderFirst) {
                orderByClause = example.getOrderByClause();
                if (orderByClause != null && orderByClause.length() > 0) {
                    example.setOrderByClause("");
                    hasFirst = true;
                }
            }

            for (int i = 0; i < orderByProperties.length; i++) {
                OrderBy orderBy = example.orderBy(orderByProperties[i]);
                if (orderByTypes[i] == OrderType.DESC) {
                    orderBy.desc();
                } else {
                    orderBy.asc();
                }
            }

            if (hasFirst) {
                example.setOrderByClause(orderByClause + "," + example.getOrderByClause());
            }
        }
        return example;
    }


    /**
     * 构建selection，根据class中字段名称来构建
     * <p>
     * ServiceSupport下其他查询<b>返回结果集<b/>的方法会调用该方法，用于动态生成查询字段
     *
     * @param example 查询标准集
     * @param clazz   对应结果集对象类
     * @return
     */
    public Example buildSelection(Example example, Class<?> clazz) {
        if (modelType != clazz) {
            if (example == null) {
                example = getExample();
            }
            String[] selections = selectionCacheMap.get(clazz);
            if (selections == null) {
                List<String> list = new ArrayList<>();
                for (EntityField entityField : Entity.getEntity(clazz).getEntityFields()) {
                    if (entityField.getAnnotation(IgnoreSelection.class) == null) {
                        String fieldName = entityField.getName();
                        if (Entity.getEntity(modelType).getEntityField(fieldName) != null) {
                            list.add(fieldName);
                        }
                    }
                }
                selections = list.toArray(new String[list.size()]);
                selectionCacheMap.put(clazz, selections);
            }
            example.selectProperties(selections);
        } else if (ignoreSelections.length > 0) {
            example.excludeProperties(ignoreSelections);
        }
        return example;
    }


    /**
     * 构建查询条件
     * <p>
     * 该部分条件添加在最后一组criteria，如果需要添加一组新的criteria，应该在调用该方法前调用example的创建criteria方法
     *
     * @param example     查询标准集
     * @param searchParam Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public Example buildExample(Example example, Object searchParam) {
        if (example == null) {
            example = getExample();
        }

        if (searchParam instanceof Condition) {
            example = ExampleHelper.buildCurrent(example, (Condition) searchParam);
        } else {
            Class<?> clazz = searchParam.getClass();
            if (List.class.isAssignableFrom(clazz)) {
                List list = (List) searchParam;
                for (Object param : list) {
                    example = buildExample(example, param);
                }
            } else if (clazz.isArray()) {
                for (int i = 0; i < Array.getLength(searchParam); i++) {
                    example = buildExample(example, Array.get(searchParam, i));
                }
            } else {
                example = ExampleHelper.buildQuery(example, searchParam);
            }
        }

        return example;
    }

    /**
     * 构建或创建一个Example
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public Example buildOrCreateExample(Object searchParam) {
        return buildOrCreateExample(searchParam, modelType, false);
    }

    /**
     * 构建或创建一个Example
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param VOClass     view object class
     * @param simple      是否简单模式
     * @return
     */
    public Example buildOrCreateExample(Object searchParam, Class<?> VOClass, boolean simple) {
        Example example = null;
        if (searchParam != null) {
            if (searchParam instanceof Example) {
                example = (Example) searchParam;
            } else {
                example = buildExample(null, searchParam);
            }
        }

        if (VOClass != modelType) {
            example = buildSelection(example, VOClass);
        }

        if (!simple) {
            example = buildCommon(example);
        }

        return example;
    }

    /**
     * 根据标准查询
     *
     * @param example 查询标准集
     * @return
     */
    public List<Model> selectByExample(Example example) {
        if (example == null) {
            return getSqlMapper().selectAll();
        } else {
            return getSqlMapper().selectByExample(example);
        }
    }

    /**
     * 根据标准查询
     *
     * @param VOClass view object class
     * @param example 查询标准集
     * @return
     */
    public <T> List<T> selectByExample(Class<T> VOClass, Example example) {
        List<Model> result;

        if (example == null) {
            result = getSqlMapper().selectAll();
        } else {
            result = getSqlMapper().selectByExample(example);
        }

        if (VOClass != modelType) {
            return SimpleBeanCopyUtil.simpleCopyList(result, VOClass);
        } else {
            return (List<T>) result;
        }
    }

    /**
     * 查找所有结果
     *
     * @return
     */
    public List<Model> findAll() {
        return findAll(modelType, false);
    }

    /**
     * 查询所有结果，并返回对应view object
     *
     * @param VOClass view object class
     * @return
     */
    public <T> List<T> findAll(Class<T> VOClass) {
        return findAll(VOClass, false);
    }

    /**
     * 查找所有结果
     *
     * @param simple  是否简单模式
     * @param VOClass view object class
     * @return
     */
    public <T> List<T> findAll(Class<T> VOClass, boolean simple) {
        return searchAll(null, VOClass, simple);
    }

    /**
     * 分页查找所有
     *
     * @param offset 偏移量
     * @param limit  页大小
     * @return
     */
    public PageResult<Model> findPage(int offset, int limit) {
        return findPage(offset, limit, modelType, false);
    }

    /**
     * 分页查找所有
     *
     * @param offset  偏移量
     * @param limit   页大小
     * @param VOClass view object class
     * @return
     */
    public <T> PageResult<T> findPage(int offset, int limit, Class<T> VOClass) {
        return findPage(offset, limit, VOClass, false);
    }

    /**
     * 分页查找所有
     *
     * @param offset  偏移量
     * @param limit   页大小
     * @param VOClass view object class
     * @param simple  是否简单模式
     * @return
     */
    public <T> PageResult<T> findPage(int offset, int limit, Class<T> VOClass, boolean simple) {
        return searchPage(null, offset, limit, VOClass, simple);
    }

    /**
     * 查找唯一数据
     *
     * @param conditions 查询条件
     * @return
     */
    public Model searchOne(Condition... conditions) {
        return searchOne(conditions, false);
    }

    /**
     * 查找唯一数据
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public Model searchOne(Object searchParam) {
        return searchOne(searchParam, false);
    }

    /**
     * 查找唯一数据
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param simple      是否简单模式
     * @return
     */
    public Model searchOne(Object searchParam, boolean simple) {
        List<Model> result = searchAll(searchParam, modelType, simple);
        if (result != null) {
            int size = result.size();
            if (size == 1) {
                return result.get(0);
            } else if (size > 1) {
                throw new BusinessException("存在多个符合条件结果");
            }
        }
        return null;
    }

    /**
     * 条件过滤查询
     *
     * @param conditions 查询条件
     * @return
     */
    public List<Model> searchAll(Condition... conditions) {
        return searchAll(conditions, modelType, false);
    }

    /**
     * 条件过滤查询
     *
     * @param VOClass    view object class
     * @param conditions 查询条件
     * @return
     */
    public <T> List<T> searchAll(Class<T> VOClass, Condition... conditions) {
        return searchAll(conditions, VOClass, false);
    }

    /**
     * 条件过滤查询
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public List<Model> searchAll(Object searchParam) {
        return searchAll(searchParam, modelType, false);
    }

    /**
     * 条件过滤查询
     *
     * @param VOClass     view object class
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public <T> List<T> searchAll(Class<T> VOClass, Object searchParam) {
        return searchAll(searchParam, VOClass, false);
    }


    /**
     * 条件过滤查询（简单模式下将不会考虑通用条件和动态条件）
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param VOClass     view object class
     * @param simple      是否简单模式
     * @return
     */
    public <T> List<T> searchAll(Object searchParam, Class<T> VOClass, boolean simple) {
        Example example = buildOrCreateExample(searchParam, VOClass, simple);
        return selectByExample(VOClass, example);
    }


    /**
     * 条件过滤分页查询
     *
     * @param offset     偏移量
     * @param limit      页大小
     * @param conditions 查询条件
     * @return
     */
    public PageResult<Model> searchPage(int offset, int limit, Condition... conditions) {
        return searchPage(conditions, offset, limit, modelType, false);
    }


    /**
     * 条件过滤分页查询
     *
     * @param offset     偏移量
     * @param limit      页大小
     * @param VOClass    view object class
     * @param conditions 查询条件
     * @return
     */
    public <T> PageResult<T> searchPage(int offset, int limit, Class<T> VOClass, Condition... conditions) {
        return searchPage(conditions, offset, limit, VOClass, false);
    }

    /**
     * 条件过滤分页查询
     *
     * @param offset      偏移量
     * @param limit       页大小
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public PageResult<Model> searchPage(int offset, int limit, Object searchParam) {
        return searchPage(searchParam, offset, limit, modelType, false);
    }

    /**
     * 条件过滤分页查询
     *
     * @param offset      偏移量
     * @param limit       页大小
     * @param VOClass     view object class
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public <T> PageResult<T> searchPage(int offset, int limit, Class<T> VOClass, Object searchParam) {
        return searchPage(searchParam, offset, limit, VOClass, false);
    }

    /**
     * 条件过滤分页查询
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public PageResult<Model> searchPage(Object searchParam) {
        return searchPage(searchParam, modelType, false);
    }

    /**
     * 条件过滤分页查询
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param VOClass     view object class
     * @return
     */
    public <T> PageResult<T> searchPage(Object searchParam, Class<T> VOClass) {
        return searchPage(searchParam, VOClass, false);
    }

    /**
     * 条件过滤分页查询
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param VOClass     view object class
     * @param simple      是否简单模式
     * @return
     */
    public <T> PageResult<T> searchPage(Object searchParam, Class<T> VOClass, boolean simple) {
        if (searchParam == null) {
            return searchPage(null, 0, OffsetPage.DEFAULT_LIMIT, VOClass, simple);
        }
        if (searchParam instanceof OffsetPage) {
            OffsetPage page = (OffsetPage) searchParam;
            return searchPage(searchParam, page.getOffset(), page.getLimit(), VOClass, simple);
        } else {
            return searchPage(searchParam, 0, OffsetPage.DEFAULT_LIMIT, VOClass, simple);
        }
    }


    /**
     * 条件过滤分页查询
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param offset      偏移量
     * @param limit       页大小
     * @param VOClass     view object class
     * @param simple      是否简单模式
     * @return
     */
    public <T> PageResult<T> searchPage(Object searchParam, int offset, int limit, Class<T> VOClass, boolean simple) {
        if (limit > OffsetPage.MAX_LIMIT) {
            limit = OffsetPage.MAX_LIMIT;
        }
        Page<T> page = PageHelper.offsetPage(offset, limit);
        List<T> result = searchAll(searchParam, VOClass, simple);
        if (result == null || result.size() == 0) {
            page.setTotal(0L);
        }
        return new PageResult<T>(page, result);
    }

    /**
     * 查询记录数
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public int searchCount(Object searchParam) {
        return searchCount(searchParam, false);
    }

    /**
     * 查询记录数
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param simple      是否简单模式
     * @return
     */
    public int searchCount(Object searchParam, boolean simple) {
        Example example = buildOrCreateExample(searchParam, modelType, simple);
        if (example == null) {
            example = getExample();
        }
        return getSqlMapper().selectCountByExample(example);
    }


    // -----------------------------------------------------
    // 修改保存删除
    // -----------------------------------------------------

    /**
     * 保存插入
     *
     * @param model 实体类
     * @return
     */
    public boolean save(Model model) {
        checkEmptyId(model);
        saveModelWrap(model);
        return getSqlMapper().insert(model) > 0;
    }

    /**
     * 如果存在主键值则进行更新，没有则进行插入
     *
     * @param model 实体类
     * @return
     */
    public boolean saveOrUpdate(Model model) {
        if (judgeHasPKValue(model)) {
            return update(model);
        } else {
            return save(model);
        }
    }

    /**
     * 更新实体类中字段不为空的数据
     *
     * @param model 实体类
     * @return
     */
    public boolean updateSelective(Model model) {
        updateModelWrap(model);
        return getSqlMapper().updateByPrimaryKeySelective(model) > 0;
    }

    /**
     * 更新整个实体类（包括空字段）
     *
     * @param model 实体类
     * @return
     */
    public boolean update(Model model) {
        updateModelWrap(model);
        return getSqlMapper().updateByPrimaryKey(model) > 0;
    }

    /**
     * 删除主键对应数据（如果是基于BaseModel的进行逻辑删除）
     *
     * @param pk 主键
     * @return
     */
    public boolean removeByPrimaryKey(Object pk) {
        if (isBaseModel) {
            Model model = getSqlMapper().selectByPrimaryKey(pk);
            if (model != null) {
                ((BaseModel) model).setDeleted(true);
                updateModelWrap(model);
                return getSqlMapper().updateByPrimaryKey(model) > 0;
            }
            return false;
        }
        return getSqlMapper().deleteByPrimaryKey(pk) > 0;
    }

    /**
     * 根据条件删除
     *
     * @param conditions 查询条件
     * @return
     */
    public int remove(Condition... conditions) {
        return remove(conditions, false);
    }

    /**
     * 根据条件删除
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @return
     */
    public int remove(Object searchParam) {
        return remove(searchParam, false);
    }

    /**
     * 根据条件删除
     *
     * @param searchParam Example、Condition、QueryCondition注解的实体类，或者是这几类的集合
     * @param simple      是否简单模式
     * @return
     */
    public int remove(Object searchParam, boolean simple) {
        Example example = buildOrCreateExample(searchParam, modelType, simple);
        if (isBaseModel) {
            try {
                Model model = modelType.newInstance();
                ((BaseModel) model).setDeleted(true);
                updateModelWrap(model);
                return getSqlMapper().updateByExampleSelective(model, example);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new SystemException(SystemExceptionCode.CODE_ERROR_CODE, "该model无法创建默认实体，没法兼容该方法");
            }
        } else {
            return getSqlMapper().deleteByExample(example);
        }
    }

    /**
     * 更新操作前需要对数据包裹，例如设置更新操作人与操作时间
     *
     * @param model 实体类
     */
    public void updateModelWrap(Model model) {
        if (isBaseModel) {
            Date now = new Date();
            UserSession userSession = UserSession.getCurrentUserSession();
            String uid = userSession == null ? "" : userSession.getUserId();
            BaseModel baseModel = (BaseModel) model;
            baseModel.setUpdateTime(now);
            baseModel.setUpdateBy(uid);
        }
    }

    /**
     * 保存操作前需要对数据包裹，例如设置创建操作人与操作时间
     *
     * @param model 实体类
     */
    public void saveModelWrap(Model model) {
        if (isBaseModel) {
            Date now = new Date();
            UserSession userSession = UserSession.getCurrentUserSession();
            String uid = userSession == null ? "" : userSession.getUserId();
            BaseModel baseModel = (BaseModel) model;
            baseModel.setCreateTime(now);
            baseModel.setCreateBy(uid);
            baseModel.setUpdateBy(uid);
            baseModel.setUpdateTime(now);
            baseModel.setDeleted(false);
        }
    }


    // -----------------------------------------------------
    //
    // -----------------------------------------------------

    /**
     * 获取一个分页
     *
     * @param query
     * @return
     */
    public <E> Page<E> getPage(OffsetPage query) {
        int limit = query.getLimit();
        int offset = query.getOffset();

        if (limit > OffsetPage.MAX_LIMIT) {
            limit = OffsetPage.MAX_LIMIT;
        }
        return PageHelper.offsetPage(offset, limit);
    }


    /**
     * 获取一个空的分页结果
     *
     * @param offsetPage 分页对象
     * @return
     */
    public PageResult getEmptyPageResult(OffsetPage offsetPage) {
        Page page = new Page(offsetPage.getOffset(), offsetPage.getLimit());
        page.setTotal(0L);
        return new PageResult(page);
    }

    /**
     * 获取一个空的分页结果
     *
     * @param page
     * @return
     */
    public <T> PageResult<T> getEmptyPageResult(Page<T> page) {
        page.setTotal(0L);
        return new PageResult<T>(page);
    }

}
