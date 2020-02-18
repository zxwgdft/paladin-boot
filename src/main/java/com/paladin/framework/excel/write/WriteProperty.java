package com.paladin.framework.excel.write;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteProperty {
	/**
	 * EXCEL列序号
	 * @return
	 */
	public int cellIndex();
	
	public String enumType() default "";

	/**输出的列标题名*/
	public String name() default "";
	/**默认值，当值为null的时候使用*/
	public String defaultValue() default "";
	/**列宽度,1代表一个英文字符*/
	public int width() default -1;
	/**是否自动适应宽度，优先级高于宽度*/
	public boolean autoWidth() default false;
	/**是否自动换行*/
	public boolean wrapText() default true;
	/**格式化*/
	public String format() default "";
	/**日期格式化字符串*/
	public String dateFormat() default "";
	/**对齐方式,见{@link org.apache.poi.ss.usermodel.CellStyle}内参数*/
	public int alignment() default org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
	
}
