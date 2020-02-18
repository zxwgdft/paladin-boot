package com.paladin.framework.excel;

public interface ISheet {
	public IRow getRow(int rowIndex);
	public int getLastRowNum();
}
