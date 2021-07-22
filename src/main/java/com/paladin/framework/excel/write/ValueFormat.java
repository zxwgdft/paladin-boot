package com.paladin.framework.excel.write;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * class of formatter for cell value
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueFormat {

    Class<? extends ValueFormatter> format();

    /**
     * 是否缓存formatter实例
     */
    boolean cache() default true;
}
