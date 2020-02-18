package com.paladin.framework.core.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询输入页面的方法
 * @author TontoZhou
 * @since 2018年7月3日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryInputMethod {
	Class<?> queryClass();	
	String viewName() default "query";
}
