package com.paladin.framework.excel.write;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface WriteProperty {

    /**
     * EXCEL列序号
     *
     * @return
     */
    int cellIndex();

    String enumType() default "";

    /**
     * 输出的列标题名
     */
    String name() default "";

    /**
     * 默认值，当值为null的时候使用
     */
    String defaultValue() default "";

    /**
     * 列宽度,1代表一个英文字符
     */
    int width() default -1;

    /**
     * 是否自动适应宽度，优先级高于宽度
     */
    boolean autoWidth() default false;

    /**
     * 是否自动换行
     */
    boolean wrapText() default true;

    /**
     * 格式化
     */
    String format() default "";

    /**
     * 日期格式化字符串
     */
    String dateFormat() default "";

    /**
     * 对齐方式
     */
    HorizontalAlignment alignment() default HorizontalAlignment.CENTER;

}
