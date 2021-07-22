package com.paladin.framework.excel.write;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface CellStyleCreator {

    CellStyle createCellStyle(Workbook workbook, CellStyle commonCellStyle, WriteColumn writeColumn);

}
