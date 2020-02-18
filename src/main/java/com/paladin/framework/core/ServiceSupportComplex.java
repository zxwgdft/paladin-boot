package com.paladin.framework.core;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.paladin.framework.common.Condition;
import com.paladin.framework.common.GeneralCriteriaBuilder;
import com.paladin.framework.common.OffsetPage;
import com.paladin.framework.common.PageResult;
import com.paladin.framework.core.configuration.mybatis.CustomJoinMapper;
import com.paladin.framework.utils.reflect.ReflectUtil;

import tk.mybatis.mapper.entity.Example;

public class ServiceSupportComplex<Model, JoinModel extends Model> extends ServiceSupport<Model> {

	private final static Logger logger = LoggerFactory.getLogger(ServiceSupportComplex.class);

	protected Class<JoinModel> joinModelType; // 业务对应类
	private CustomJoinMapper<JoinModel, Model> joinMapper;

	@SuppressWarnings("unchecked")
	public ServiceSupportComplex() {
		super();
		Class<?> clazz = ReflectUtil.getSuperClassArgument(this.getClass(), ServiceSupportComplex.class, 1);
		if (clazz == null) {
			logger.warn("[" + this.getClass().getName() + "]的实现类没有明确定义[" + ServiceSupportComplex.class.getName() + "]的泛型");
		}

		joinModelType = (Class<JoinModel>) clazz;
	}

	public CustomJoinMapper<JoinModel, Model> getJoinMapper() {
		return joinMapper;
	}

	public void setJoinMapper(CustomJoinMapper<JoinModel, Model> joinMapper) {
		this.joinMapper = joinMapper;
	}

	/**
	 * 根据主键获取
	 * @param pk
	 * @return
	 */
	public JoinModel getJoin(Object pk) {
		return getJoinMapper().getJoin(pk);
	}
	
	/**
	 * 查找所有结果
	 * 
	 * @return
	 */
	public List<JoinModel> findJoinAll() {
		return findJoinAll(false);
	}

	/**
	 * 查找所有结果
	 * 
	 * @param simple
	 *            是否简单模式
	 * @return
	 */
	public List<JoinModel> findJoinAll(boolean simple) {
		if (simple) {
			return getJoinMapper().selectJoinAll();
		}

		Example example = null;

		if (hasCommonCondition) {
			if (hasDynamicCondition) {
				example = GeneralCriteriaBuilder.buildAnd(modelType, commonConditions);
				example = buildDynamicCondition(example);
			} else {
				// commonExample 已经排序过
				return getJoinMapper().selectJoinByExample(commonExample);
			}
		} else if (hasDynamicCondition) {
			example = buildDynamicCondition(null);
		}

		example = buildOrderBy(example);

		if (example == null) {
			return getJoinMapper().selectJoinAll();
		} else {
			return getJoinMapper().selectJoinByExample(example);
		}
	}

	/**
	 * 分页查找
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public PageResult<JoinModel> findJoinPage(int offset, int limit) {
		return findJoinPage(offset, limit, false);
	}

	/**
	 * 分页查找
	 * 
	 * @param offset
	 * @param limit
	 * @param simple
	 *            是否简单模式
	 * @return
	 */
	public PageResult<JoinModel> findJoinPage(int offset, int limit, boolean simple) {
		if (limit > OffsetPage.MAX_LIMIT) {
			limit = OffsetPage.MAX_LIMIT;
		}

		Page<JoinModel> page = PageHelper.offsetPage(offset, limit);
		try {
			List<JoinModel> result = findJoinAll(simple);
			if (result == null || result.size() == 0) {
				page.setTotal(0L);
			}
			return new PageResult<JoinModel>(page);
		} finally {
			PageHelper.clearPage();
		}
	}
	
	
	/**
	 * 条件过滤查询(非简单模式)
	 * 
	 * @param searchParam
	 * @return
	 */
	public List<JoinModel> searchJoinAll(Object searchParam) {
		return searchJoinAll(searchParam, false);
	}

	/**
	 * 条件过滤查询（简单模式下将不会考虑通用条件和动态条件）
	 * 
	 * @param searchParam
	 *            <ul>
	 *            <li>{@link Condition}单个条件或者它的数组和集合</li>
	 *            <li>基于注解和{@link GeneralCriteriaBuilder}的查询类实例</li>
	 *            </ul>
	 * @param simple
	 *            是否简单模式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JoinModel> searchJoinAll(Object searchParam, boolean simple) {

		Example example = null;

		if (searchParam instanceof Example) {

			example = (Example) searchParam;

		} else if (searchParam instanceof Condition) {

			Condition condition = (Condition) searchParam;
			example = GeneralCriteriaBuilder.buildAnd(modelType, condition);

		} else {

			Class<?> clazz = searchParam.getClass();

			if (List.class.isAssignableFrom(clazz)) {
				List<Condition> list = (List<Condition>) searchParam;
				/*
				 * 对于list，array暂时只处理Condition情况，对于其他多条件传入日后补充
				 */
				example = GeneralCriteriaBuilder.buildAnd(modelType, list);
			} else if (clazz.isArray()) {
				Condition[] array = (Condition[]) searchParam;
				example = GeneralCriteriaBuilder.buildAnd(modelType, Arrays.asList(array));
			} else {
				example = GeneralCriteriaBuilder.buildQuery(modelType, searchParam);
			}
		}

		// 如果是简单模式，则不考虑通用条件和动态条件
		if (!simple) {

			if (hasCommonCondition) {

				if (example != null) {
					example = GeneralCriteriaBuilder.buildAnd(example, commonConditions);
				}

				if (hasDynamicCondition) {
					example = buildDynamicCondition(example);
				} else {
					if (example == null) {
						return getJoinMapper().selectJoinByExample(commonExample);
					}
				}
			} else if (hasDynamicCondition) {
				example = buildDynamicCondition(example);
			}

			example = buildOrderBy(example);
		}

		if (example == null) {
			return getJoinMapper().selectJoinAll();
		} else {
			return getJoinMapper().selectJoinByExample(example);
		}
	}

	/**
	 * 条件过滤分页查询（非简单模式）
	 * 
	 * @param searchParam
	 * @return
	 */
	public PageResult<JoinModel> searchJoinPage(Object searchParam) {
		return searchJoinPage(searchParam, false);
	}

	/**
	 * 条件过滤分页查询（简单模式下将不会考虑通用条件和动态条件）
	 * 
	 * @param searchParam
	 * @param simple
	 *            是否简单模式
	 * @return
	 */
	public PageResult<JoinModel> searchJoinPage(Object searchParam, boolean simple) {
		if (searchParam == null)
			return searchJoinPage(searchParam, 0, OffsetPage.DEFAULT_LIMIT, simple);

		if (searchParam instanceof OffsetPage) {
			OffsetPage page = (OffsetPage) searchParam;
			return searchJoinPage(searchParam, page.getOffset(), page.getLimit(), simple);
		} else {
			return searchJoinPage(searchParam, 0, OffsetPage.DEFAULT_LIMIT, simple);
		}
	}

	/**
	 * 条件过滤分页查询（非简单模式）
	 * 
	 * @param searchParam
	 * @param offset
	 * @param limit
	 * @return
	 */
	public PageResult<JoinModel> searchJoinPage(Object searchParam, int offset, int limit) {
		return searchJoinPage(searchParam, offset, limit, false);
	}

	/**
	 * 条件过滤分页查询（非简单模式）
	 * @param searchParam
	 * @param page
	 * @return
	 */
	public PageResult<JoinModel> searchJoinPage(Object searchParam, OffsetPage page) {
		return searchJoinPage(searchParam, page.getOffset(), page.getLimit(), false);
	}

	/**
	 * 条件过滤分页查询（简单模式下将不会考虑通用条件和动态条件）
	 * 
	 * @param searchParam
	 * @param offset
	 * @param limit
	 * @param simple
	 *            是否简单模式
	 * @return
	 */
	public PageResult<JoinModel> searchJoinPage(Object searchParam, int offset, int limit, boolean simple) {

		if (limit > OffsetPage.MAX_LIMIT) {
			limit = OffsetPage.MAX_LIMIT;
		}

		Page<JoinModel> page = PageHelper.offsetPage(offset, limit);
		try {
			List<JoinModel> result = searchJoinAll(searchParam, simple);
			if (result == null || result.size() == 0) {
				page.setTotal(0L);
			}
			return new PageResult<JoinModel>(page);
		} finally {
			PageHelper.clearPage();
		}
	}

}
