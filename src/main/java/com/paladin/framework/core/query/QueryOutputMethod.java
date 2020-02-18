package com.paladin.framework.core.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询输出结果方法
 * @author TontoZhou
 * @since 2018年7月3日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryOutputMethod {
	Class<?> queryClass();	
	/**
	 * 第几个参数，0开始
	 * @return
	 */
	int paramIndex();
}
