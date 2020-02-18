package com.paladin.framework.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class DefaultSheet implements ISheet{
	
	private Sheet sheet;
	
	public DefaultSheet(Sheet sheet)
	{
		this.sheet=sheet;
	}
	
	@Override
	public IRow getRow(int rowIndex) {
		if(sheet==null)
			return null;
		
		Row row= sheet.getRow(rowIndex);
		if(row!=null)
			return new DefaultRow(row);
		return null;
	}

	@Override
	public int getLastRowNum() {
		if(sheet==null)
			return -1;
		return sheet.getLastRowNum();
	}
	
}
