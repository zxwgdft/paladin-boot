package com.paladin.common.core.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 *
 * @author TontoZhou
 * @since 2018年7月3日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 模块名称，缺省取class name
     * @return
     */
    String model() default "";

    /**
     * 操作名称，缺省取method name
     * @return
     */
    String operate() default "";

}
