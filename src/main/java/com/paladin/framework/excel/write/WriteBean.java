package com.paladin.framework.excel.write;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 子列注释
 * 
 * @author TontZhou
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteBean {
	public Class<?> type() default Object.class;
}
