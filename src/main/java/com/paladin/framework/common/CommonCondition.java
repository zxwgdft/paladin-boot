package com.paladin.framework.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonCondition {
	
	public String name();
	public QueryType type();
	public String value() default "";
	public Class<?> valueType() default String.class;
}
