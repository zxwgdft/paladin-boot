package com.paladin.framework.excel.write;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface CellStyleCreator {

    CellStyle createCellStyle(Workbook workbook, CellStyle commonCellStyle, WriteColumn writeColumn, Object data);

    /**
     * @return 是否样式通用，不通用则每个cell都会创建一次CellStyle
     */
    default boolean isStyleUniversal() {
        return true;
    }

}
