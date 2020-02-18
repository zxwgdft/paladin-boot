package com.paladin.framework.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCondition {
	
	public String name() default "";
	public QueryType type();
	
	public boolean nullable() default false;
	
	/**
	 * 查询条件所属实体类，用于连接查询时标明该条件属于哪个连接表
	 * @return
	 */
	public Class<?> entityClass() default Object.class;
	
	/**
	 * 由于between需要两个值，所以通过该ID找到两个值
	 * @return
	 */
	public String betweenId() default "";
}
