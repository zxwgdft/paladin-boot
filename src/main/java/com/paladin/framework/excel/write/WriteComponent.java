package com.paladin.framework.excel.write;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 数据访问接口
 *
 * @author TontZhou
 */
public interface WriteComponent {

    /**
     * 数据剥离
     *
     * @param data 源数据
     * @return 返回经过剥离的有效的数据
     */
    Object peelData(Object data);

    /**
     * 写数据到EXCEL
     *
     * @param data            具体数据
     * @param sheet           工作簿
     * @param workbook        工作空间
     * @param rowNum          行号
     * @param rowSpan         跨行数
     * @param commonCellStyle 通用样式
     * @return 返回总共写占用了多少行
     */
    int write(Object data, Sheet sheet, Workbook workbook, int rowNum, int rowSpan, CellStyle commonCellStyle);

}
