package com.paladin.framework.excel.write;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public abstract class WriteRow  implements WriteComponent{
	
	List<WriteColumn> columns;
	List<WriteRow> subRows;	
	
	public List<WriteColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<WriteColumn> columns) {
		this.columns = columns;
	}

	public List<WriteRow> getSubRows() {
		return subRows;
	}

	public void setSubRows(List<WriteRow> subRows) {
		this.subRows = subRows;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int write(Object data, Sheet sheet, Workbook workbook, int rowNum, int span, CellStyle commonCellStyle) {
		if (data == null) {
			return 0;
		}

		Class<?> type = data.getClass();
		if (Collection.class.isAssignableFrom(type)) {
			Collection coll = (Collection) data;
			int start = rowNum;
			for (Object obj : coll) {
				rowNum += writeOne(obj, sheet, workbook, rowNum, span, commonCellStyle);
			}
			return rowNum - start;
		} else if (type.isArray()) {
			int length = Array.getLength(data);
			int start = rowNum;
			for (int i = 0; i < length; i++) {
				Object item = Array.get(data, i);
				rowNum += writeOne(item, sheet, workbook, rowNum, span, commonCellStyle);
			}
			return rowNum - start;
		} else {
			return writeOne(data, sheet, workbook, rowNum, span, commonCellStyle);
		}
	}


	/**
	 * 写EXCEL
	 * 
	 * @param data
	 *            具体数据
	 * @param sheet
	 *            工作簿
	 * @param workbook
	 *            工作空间
	 * @param rowNum
	 *            行号
	 * @param span
	 *            跨行数
	 * @param commonCellStyle
	 *            通用样式
	 * @return
	 */
	public int writeOne(Object data, Sheet sheet, Workbook workbook, int rowNum, int span, CellStyle commonCellStyle) {
		int maxSubSpan = 0;

		if (subRows != null && subRows.size() > 0) {
			for (WriteRow writeRow : subRows) {
				Object subData = writeRow.peelData(data);
				int s = writeRow.write(subData, sheet, workbook, rowNum, span, commonCellStyle);
				maxSubSpan = Math.max(s, maxSubSpan);
			}
		}

		if (columns != null && columns.size() > 0) {
			span = Math.max(span, maxSubSpan);
			for (WriteColumn writeColumn : columns) {
				Object columnData = writeColumn.peelData(data);
				writeColumn.write(columnData, sheet, workbook, rowNum, span, commonCellStyle);
			}
		} else {
			span = maxSubSpan;
		}

		return span;
	}
}
