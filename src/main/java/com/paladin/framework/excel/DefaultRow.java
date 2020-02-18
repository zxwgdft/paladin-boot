package com.paladin.framework.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class DefaultRow implements IRow {

	private Row row;

	public DefaultRow(Row row) {
		this.row = row;
	}

	@Override
	public ICell getCell(int cellIndex) {
		if (row == null)
			return null;
		Cell cell = row.getCell(cellIndex);
		if (cell != null)
			return new DefaultCell(cell);
		return null;
	}

	@Override
	public int getLastCellNum() {
		return row.getLastCellNum();
	}

}
