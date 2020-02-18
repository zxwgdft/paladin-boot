package com.paladin.framework.excel.write;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 数据访问接口
 * 
 * @author TontZhou
 * 
 */
public interface WriteComponent {

	/**
	 * 数据剥离
	 * 
	 * @param data
	 *            源数据
	 * @return 返回经过剥离的有效的数据
	 */
	public Object peelData(Object data);

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
	public int write(Object data, Sheet sheet, Workbook workbook, int rowNum, int span, CellStyle commonCellStyle);

}
