package com.paladin.framework.excel;

/**
 * 
 * 属性验证接口
 * 
 * @author TontoZhou
 *
 */
public interface PropertyValidate {
	/**
	 * 验证属性字段是否正确
	 * @param origin 原始数据，可能是父类或者上层数据，在属性直接有互动比较的情况下有用
	 * @param value	具体验证的值
	 * @return
	 */
	public boolean validate(Object origin,Object value);
	
	public String getMessage();
}
