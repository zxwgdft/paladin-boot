package com.paladin.framework.excel.write;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public abstract class WriteRow implements WriteComponent {

    private List<WriteColumn> columns;
    private List<WriteRow> subRows;

    @SuppressWarnings("rawtypes")
    @Override
    public int write(Object data, Sheet sheet, Workbook workbook, int rowNum, int rowSpan, CellStyle commonCellStyle) {
        if (data == null) {
            return 0;
        }

        Class<?> type = data.getClass();
        if (Collection.class.isAssignableFrom(type)) {
            Collection coll = (Collection) data;
            int start = rowNum;
            for (Object obj : coll) {
                rowNum += writeOne(obj, sheet, workbook, rowNum, rowSpan, commonCellStyle);
            }
            return rowNum - start;
        } else if (type.isArray()) {
            int length = Array.getLength(data);
            int start = rowNum;
            for (int i = 0; i < length; i++) {
                Object item = Array.get(data, i);
                rowNum += writeOne(item, sheet, workbook, rowNum, rowSpan, commonCellStyle);
            }
            return rowNum - start;
        } else {
            return writeOne(data, sheet, workbook, rowNum, rowSpan, commonCellStyle);
        }
    }


    /**
     * 写EXCEL
     *
     * @param data            具体数据
     * @param sheet           工作簿
     * @param workbook        工作空间
     * @param rowNum          行号
     * @param rowSpan         跨行数
     * @param commonCellStyle 通用样式
     * @return
     */
    public int writeOne(Object data, Sheet sheet, Workbook workbook, int rowNum, int rowSpan, CellStyle commonCellStyle) {
        int maxSubSpan = 0;

        if (subRows != null && subRows.size() > 0) {
            for (WriteRow writeRow : subRows) {
                Object subData = writeRow.peelData(data);
                int s = writeRow.write(subData, sheet, workbook, rowNum, rowSpan, commonCellStyle);
                maxSubSpan = Math.max(s, maxSubSpan);
            }
        }

        if (columns != null && columns.size() > 0) {
            rowSpan = Math.max(rowSpan, maxSubSpan);
            for (WriteColumn writeColumn : columns) {
                Object columnData = writeColumn.peelData(data);
                writeColumn.write(columnData, sheet, workbook, rowNum, rowSpan, commonCellStyle);
            }
        } else {
            rowSpan = maxSubSpan;
        }

        return rowSpan;
    }

    /**
     * 如果需要复用，则在每次使用前应该调用该方法清除历史数据，例如历史样式（他是无法多个workbook复用）
     */
    public void reset() {
        if (subRows != null) {
            for (WriteRow writeRow : subRows) {
                writeRow.reset();
            }
        }

        if (columns != null) {
            for (WriteColumn column : columns) {
                column.reset();
            }
        }
    }
}
