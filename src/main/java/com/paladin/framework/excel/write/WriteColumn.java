package com.paladin.framework.excel.write;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import com.paladin.framework.utils.convert.DateFormatUtil;

/**
 * 用于写Excel的列
 *
 * @author TontZhou
 */
public abstract class WriteColumn implements WriteComponent {

    // ID
    private String id;

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

    // 值格式化接口，可以为NULL
    private ValueFormator valueFormator;

    // 枚举类型
    private String enumType;
    // 是否多个
    private boolean multiple;

    // 日期时间格式化字符串
    private String dateFormat;
    private SimpleDateFormat dateFormatter;

    // cell样式
    private CellStyle style;

    @Override
    public int write(Object data, Sheet sheet, Workbook workbook, int rowNum, int span, CellStyle commonCellStyle) {

        int cellIndex = getCellIndex();

        Row row = sheet.getRow(rowNum);
        if (row == null)
            row = sheet.createRow(rowNum);

        Cell cell = row.createCell(cellIndex);

        setCellValue(cell, data);

        // 固定宽度
        if (!isAutoWidth() && getWidth() > 0) {
            sheet.setColumnWidth(cellIndex, getWidth() * 256);
        }

        if (span > 1) {
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + span - 1, cellIndex, cellIndex));
        }

        cell.setCellStyle(getCellStyle(workbook, commonCellStyle));

        return span;
    }

    /**
     * <h2>获取填入的值</h2>
     * <p>
     * 优先考虑{@link ValueFormator}
     * 实例的转化方法,该方法用于缺省情况下
     * </p>
     *
     * @param cell
     * @param value 具体数据
     * @return
     */
    public void setCellValue(Cell cell, Object value) {

        String show = "";

        if (getValueFormator() != null) {
            show = getValueFormator().format(value);
        } else if (value == null) {
            show = getDefaultEmptyValue();
        } else {
            if (getEnumType() != null) {
                cell.setCellValue(getEnumName(value));
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Float) {
                cell.setCellValue((Float) value);
            } else if (value instanceof Date) {
                if (dateFormat != null && dateFormat.length() > 0) {
                    if (dateFormatter == null) {
                        dateFormatter = DateFormatUtil.getThreadSafeFormat(dateFormat);
                    }
                    cell.setCellValue(dateFormatter.format((Date) value));
                } else {
                    cell.setCellValue((Date) value);
                }
            } else if (value instanceof Calendar) {
                cell.setCellValue((Calendar) value);
            } else {
                cell.setCellValue(value.toString());
            }
            return;
        }
        cell.setCellValue(show);
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
     * @return
     */
    public CellStyle getCellStyle(Workbook workbook, CellStyle commonCellStyle) {
        if (style == null)
            style = createCellStyle(workbook, commonCellStyle);
        return style;
    }

    public CellStyle resetCellStyle() {
        return style = null;
    }

    private CellStyle createCellStyle(Workbook workbook, CellStyle commonCellStyle) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // copy通用样式
        if (commonCellStyle != null)
            style.cloneStyleFrom(commonCellStyle);

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 列序号，起始0
     */
    public int getCellIndex() {
        return cellIndex;
    }

    /**
     * 列序号，起始0
     */
    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    /**
     * 列名
     */
    public String getName() {
        return name;
    }

    /**
     * 列名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 默认值，当值为null的时候使用
     */
    public String getDefaultEmptyValue() {
        return defaultEmptyValue;
    }

    /**
     * 默认值，当值为null的时候使用
     */
    public void setDefaultEmptyValue(String defaultEmptyValue) {
        this.defaultEmptyValue = defaultEmptyValue;
    }

    /**
     * 列宽度,1代表一个英文字符
     */
    public int getWidth() {
        return width;
    }

    /**
     * 列宽度,1代表一个英文字符
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 是否自动适应宽度，优先级高于宽度
     */
    public boolean isAutoWidth() {
        return autoWidth;
    }

    /**
     * 是否自动适应宽度，优先级高于宽度
     */
    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    /**
     * 是否自动换行
     */
    public boolean isWrapText() {
        return wrapText;
    }

    /**
     * 是否自动换行
     */
    public void setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
    }

    /**
     * 格式化
     */
    public String getFormat() {
        return format;
    }

    /**
     * 格式化
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 对齐方式
     */
    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    /**
     * 对齐方式
     */
    public void setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
    }

    /**
     * 值格式化接口，可以为NULL
     */
    public ValueFormator getValueFormator() {
        return valueFormator;
    }

    /**
     * 值格式化接口，可以为NULL
     */
    public void setValueFormator(ValueFormator valueFormator) {
        this.valueFormator = valueFormator;
    }

    /**
     * 常量·字典类型
     */
    public String getEnumType() {
        return enumType;
    }

    /**
     * 常量·字典类型
     */
    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

    /**
     * 日期时间格式化字符串
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * 日期时间格式化字符串
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nID：").append(id).append("\n列名：").append(name).append("\n列序号：").append(cellIndex);
        return sb.toString();
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

}
