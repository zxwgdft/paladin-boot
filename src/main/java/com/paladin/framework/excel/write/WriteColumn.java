package com.paladin.framework.excel.write;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用于写Excel的列
 *
 * @author TontZhou
 */
@Getter
@Setter
public abstract class WriteColumn implements WriteComponent {

    // 对应excel的列序号
    private int cellIndex = -1;

    // 对应excel上的列名
    private String name;

    // 默认空值显示
    private String defaultEmptyValue = "";

    // 列宽
    private int width = -1;

    // 自动适应宽度
    private boolean autoWidth = false;

    // 自动换行
    private boolean wrapText = true;

    // excel自带的格式化字符串
    private String format = "";

    // 对齐方式
    private HorizontalAlignment alignment = HorizontalAlignment.CENTER;

    // 枚举类型
    private String enumType;

    // 是否多个
    private boolean multiple;

    // 日期格式化
    private DateFormat dateFormatter;

    // 值格式化接口，可以为NULL
    private ValueFormatter valueFormatter;

    // cell样式创建器
    private CellStyleCreator cellStyleCreator;

    // cell样式是否通用
    private boolean isStyleUniversal = true;

    // cell样式
    private CellStyle style;

    private String booleanTrueValue = "是";
    private String booleanFalseValue = "否";

    @Override
    public int write(Object data, Sheet sheet, Workbook workbook, int rowNum, int rowSpan, CellStyle commonCellStyle) {
        int cellIndex = getCellIndex();

        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }

        Cell cell = row.createCell(cellIndex);
        setCellValue(cell, data);

        // 固定宽度
        if (!isAutoWidth() && getWidth() > 0) {
            sheet.setColumnWidth(cellIndex, getWidth() * 256);
        }

        if (rowSpan > 1) {
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + rowSpan - 1, cellIndex, cellIndex));
        }

        cell.setCellStyle(getCellStyle(workbook, commonCellStyle, data));

        return rowSpan;
    }

    /**
     * <h2>获取填入的值</h2>
     * <p>
     * 优先考虑{@link ValueFormatter}实例的转化方法,该方法用于缺省情况下
     * </p>
     */
    public void setCellValue(Cell cell, Object value) {
        if (valueFormatter != null) {
            cell.setCellValue(valueFormatter.format(value));
        } else if (value == null) {
            cell.setCellValue(getDefaultEmptyValue());
        } else {
            if (getEnumType() != null) {
                cell.setCellValue(getEnumName(value));
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue(((Boolean) value) ? booleanTrueValue : booleanFalseValue);
            } else if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Float) {
                cell.setCellValue((Float) value);
            } else if (value instanceof Date) {
                if (dateFormatter != null) {
                    cell.setCellValue(dateFormatter.format((Date) value));
                } else {
                    cell.setCellValue((Date) value);
                }
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    /**
     * 获取枚举常量名称
     *
     * @param value
     * @return
     */
    public abstract String getEnumName(Object value);

    /**
     * 创建样式，不同workbook不能共用style
     *
     * @param workbook        工作簿
     * @param commonCellStyle 基础样式，可以为NULL
     * @param data            cell值
     * @return
     */
    public CellStyle getCellStyle(Workbook workbook, CellStyle commonCellStyle, Object data) {
        if (!isStyleUniversal || style == null) {
            style = createCellStyle(workbook, commonCellStyle, data);
        }
        return style;
    }

    /**
     * 如果需要复用，则在每次使用前应该调用该方法清除历史数据，例如历史样式（他是无法多个workbook复用）
     */
    public void reset() {
        style = null;
    }

    /**
     * 创建当前列的样式
     */
    protected CellStyle createCellStyle(Workbook workbook, CellStyle commonCellStyle, Object data) {
        if (cellStyleCreator != null) {
            return cellStyleCreator.createCellStyle(workbook, commonCellStyle, this, data);
        } else {
            // 缺省情况下默认创建样式
            CellStyle style = workbook.createCellStyle();

            // copy通用样式
            if (commonCellStyle != null) {
                style.cloneStyleFrom(commonCellStyle);
            }

            String format = getFormat();
            if (!"".equals(format)) {
                // 是否为excel自带格式化定义
                short fmt = (short) BuiltinFormats.getBuiltinFormat(format);
                fmt = fmt == -1 ? workbook.createDataFormat().getFormat(format) : fmt;
                style.setDataFormat(fmt);
            }

            style.setWrapText(isWrapText());
            style.setAlignment(getAlignment());

            return style;
        }
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat != null && dateFormat.length() > 0) {
            dateFormatter = new SimpleDateFormat(dateFormat);
        }
    }

    public void setCellStyleCreator(CellStyleCreator cellStyleCreator) {
        this.cellStyleCreator = cellStyleCreator;
        this.isStyleUniversal = cellStyleCreator.isStyleUniversal();
    }

}
