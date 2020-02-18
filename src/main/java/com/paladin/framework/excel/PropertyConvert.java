package com.paladin.framework.excel;

/**
 * 
 * 属性转化接口
 * 
 * @author TontoZhou
 * 
 */
public interface PropertyConvert<T> {

	public T convert(Object obj) throws ConvertException;
}
