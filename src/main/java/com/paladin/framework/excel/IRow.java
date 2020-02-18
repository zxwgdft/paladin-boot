package com.paladin.framework.excel;

public interface IRow {
	public ICell getCell(int cellIndex);
	public int getLastCellNum();
}
