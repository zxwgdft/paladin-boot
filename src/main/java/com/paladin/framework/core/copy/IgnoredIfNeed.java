package com.paladin.framework.core.copy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释在拷贝源对象的GET方法上
 * 
 * @author TontoZhou
 * @since 2018年7月5日
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoredIfNeed {

}
