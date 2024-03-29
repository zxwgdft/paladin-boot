package com.paladin.common.core.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的简单权限判断注解
 * <p>
 * 需要权限code
 *
 * @author TontoZhou
 * @since 2020/3/19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {

    // 一般为权限code
    String value();

}
