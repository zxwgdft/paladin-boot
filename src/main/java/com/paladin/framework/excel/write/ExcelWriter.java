package com.paladin.framework.excel.write;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelWriter<T> {

    /* 通用的样式 */
    private CellStyle commonCellStyle;
    /* 标题列样式 */
    private CellStyle titleCellStyle;

    private Workbook workbook;

    /* 工作簿最大行数，-1则没有限制 */
    private int sheetMaxSize = -1;
    /* 当前输入行 */
    private int currentRowIndex = -1;
    /* 当前sheet序号 */
    private int currentSheetIndex = 1;

    /* 当前工作簿 */
    private Sheet currentSheet;
    /* 写行描述 */
    private WriteRow writeRow;

    /**
     * 创建对象
     *
     * @param workbook poi工作空间对象
     * @param writeRow 写入数据行描述
     * @throws ExcelWriteException
     */
    public ExcelWriter(Workbook workbook, WriteRow writeRow) throws ExcelWriteException {
        this(workbook, writeRow, null, null);
    }

    /**
     * 创建对象
     *
     * @param workbook    poi工作空间对象
     * @param writeRow    写入数据行描述
     * @param commonStyle 通用cell的样式
     * @param titleStyle  标题cell的样式
     * @throws ExcelWriteException
     */
    public ExcelWriter(Workbook workbook, WriteRow writeRow, CellStyle commonStyle, CellStyle titleStyle) throws ExcelWriteException {

        this.workbook = workbook;

        if (commonStyle == null) {
            commonCellStyle = workbook.createCellStyle();
            commonCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            commonCellStyle = commonStyle;
        }

        if (titleStyle == null) {
            titleCellStyle = workbook.createCellStyle();

            Font font = workbook.createFont();
            font.setBold(true);

            titleCellStyle.setFont(font);
            titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCellStyle.setWrapText(true);
        } else {
            titleCellStyle = titleStyle;
        }

        setWriteRow(writeRow);
    }

    /**
     * 增加一个工作簿，并写入标题（第一行）
     */
    public void openNewSheet() {
        openNewSheet("sheet&" + currentSheetIndex, 0);
    }

    /**
     * 增加一个工作簿，并写入标题（第一行）
     */
    public void openNewSheet(String sheetName) {
        openNewSheet(sheetName, 0);
    }

    /**
     * 增加一个工作簿，并在指定行写入标题
     */
    public void openNewSheet(String sheetName, int startIndex) {
        if (sheetName == null || sheetName.length() == 0) {
            sheetName = "sheet&" + currentSheetIndex;
        }

        currentSheet = workbook.createSheet(sheetName);
        currentSheetIndex++;
        currentRowIndex = startIndex;
        writeTitle();
    }

    /**
     * 写标题行
     */
    private void writeTitle() {
        writeRowTitle(currentSheet.createRow(currentRowIndex++), writeRow);
    }

    /**
     * 写标题GeneratedValue
     *
     * @param row
     * @param wr
     */
    private void writeRowTitle(Row row, WriteRow wr) {
        List<WriteColumn> columns = wr.getColumns();

        if (columns != null && columns.size() > 0) {
            for (WriteColumn column : columns) {
                Cell cell = row.createCell(column.getCellIndex());
                cell.setCellValue(column.getName());

                // 固定宽度
                if (!column.isAutoWidth() && column.getWidth() > 0) {
                    currentSheet.setColumnWidth(column.getCellIndex(), column.getWidth() * 256);
                }

                cell.setCellStyle(titleCellStyle);
            }
        }

        List<WriteRow> wrows = wr.getSubRows();

        if (wrows != null && wrows.size() > 0) {
            for (WriteRow wrow : wrows)
                writeRowTitle(row, wrow);
        }
    }

    /**
     * 从当前行往下写多行
     *
     * @param data
     */
    public void write(List<T> data) {
        for (T item : data) {
            writeOne(item);
        }
    }

    /**
     * 从当前行往下写多行
     *
     * @param data
     */
    public void write(T[] data) {
        for (T item : data) {
            writeOne(item);
        }
    }

    /**
     * 从当前行往下写多行
     *
     * @param data
     */
    public void writeOne(T data) {
        currentRowIndex += writeRow.write(data, currentSheet, workbook, currentRowIndex, 1, commonCellStyle);
        if (sheetMaxSize > 0 && currentRowIndex > sheetMaxSize) {
            openNewSheet();
        }
    }


    /**
     * 输出
     *
     * @param output
     * @throws IOException
     */
    public void output(OutputStream output) throws IOException {
        workbook.write(output);
    }

    /**
     * 当前行号，0开始
     */
    public int getCurrentRowIndex() {
        return currentRowIndex;
    }

    /**
     * 当前行号，0开始
     */
    public void setCurrentRowIndex(int currentRowIndex) {
        if (currentRowIndex >= 0) {
            this.currentRowIndex = currentRowIndex;
        }
        throw new RuntimeException("row index:" + currentRowIndex + " is invalid");
    }

    public WriteRow getWriteRow() {
        return writeRow;
    }

    public void setWriteRow(WriteRow writeRow) {
        this.writeRow = writeRow;
    }

    /**
     * 通用样式
     */
    public CellStyle getCommonCellStyle() {
        return commonCellStyle;
    }

    /**
     * 通用样式
     */
    public void setCommonCellStyle(CellStyle commonCellStyle) {
        if (commonCellStyle != null) {
            this.commonCellStyle = commonCellStyle;
        }
        throw new RuntimeException("common cell style can't be null");
    }

    /**
     * 标题样式
     */
    public CellStyle getTitleCellStyle() {
        return titleCellStyle;
    }

    /**
     * 标题样式
     */
    public void setTitleCellStyle(CellStyle titleCellStyle) {
        if (titleCellStyle != null) {
            this.titleCellStyle = titleCellStyle;
        }
        throw new RuntimeException("title cell style can't be null");
    }

    /**
     * 每个工作簿最大行数
     */
    public int getSheetMaxSize() {
        return sheetMaxSize;
    }

    /**
     * 每个工作簿最大行数
     */
    public void setSheetMaxSize(int sheetMaxSize) {
        if (sheetMaxSize > 1) {
            this.sheetMaxSize = sheetMaxSize;
        }
        throw new RuntimeException("sheet max size:" + currentSheetIndex + " is invalid");
    }
}
