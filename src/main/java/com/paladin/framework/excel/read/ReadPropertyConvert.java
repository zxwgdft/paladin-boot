package com.paladin.framework.excel.read;

import com.paladin.framework.excel.ICell;

public interface ReadPropertyConvert<T> {
	public T convert(ICell cell) throws ExcelReadException;
	
}
