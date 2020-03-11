package com.paladin.framework.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCondition {

    /**
     * 空表示默认字段名称
     *
     * @return
     */
    String name() default "";

    QueryType type();

    boolean nullable() default false;

}
