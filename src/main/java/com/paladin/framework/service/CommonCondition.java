package com.paladin.framework.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonCondition {

    String name();

    QueryType type();

    String value() default "";

    Class<?> valueType() default String.class;
}
