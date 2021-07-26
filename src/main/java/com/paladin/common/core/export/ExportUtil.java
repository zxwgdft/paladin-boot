package com.paladin.common.core.export;

import com.paladin.common.core.export.ExportCondition.ExportColumn;
import com.paladin.framework.excel.write.*;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.io.TemporaryFileHelper;
import com.paladin.framework.io.TemporaryFileHelper.TemporaryFileOutputStream;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportUtil {

    public static <T> String export(ExportCondition condition, List<T> data, Class<T> exportClass) throws IOException, ExcelWriteException {
        String fileType = condition.getFileType();
        if (ExportCondition.FILE_TYPE_EXCEL.equals(fileType)) {
            try (TemporaryFileOutputStream output = TemporaryFileHelper.getFileOutputStream(condition.getFileName(), "xlsx")) {
                exportExcel(condition, data, exportClass, output);
                return output.getFileRelativeUrl();
            }
        } else {
            throw new BusinessException("导出文件类型[" + fileType + "]不存在！");
        }
    }

    public static <T> void exportExcel(ExportCondition condition, List<T> data, Class<T> exportClass, OutputStream output)
            throws ExcelWriteException, IOException {
        if (data == null || data.size() == 0) {
            throw new BusinessException("需要导出的数据为空");
        }

        WriteRow writeRow = createExcelWriteRow(condition, exportClass);

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        ExcelWriter<T> writer = new ExcelWriter<T>(workbook, writeRow);
        writer.openNewSheet("导出数据");
        writer.write(data);
        writer.output(output);
    }

    public static WriteRow createExcelWriteRow(ExportCondition condition, Class<?> exportClass) {
        List<ExportColumn> columns = condition.getColumns();
        if (columns == null || columns.size() == 0) {
            throw new BusinessException("需要导出的Excel列为空");
        }

        List<WriteColumn> writeColumns = new ArrayList<>();
        Map<String, ValueFormatter> valueFormatMap = condition.getValueFormatMap();
        Map<String, CellStyleCreator> cellStyleCreatorMap = condition.getCellStyleCreatorMap();


        int cellIndex = 0;
        for (ExportColumn column : columns) {
            String field = column.getField();
            SimpleWriteColumn writeColumn = SimpleWriteColumn.newInstance(field, exportClass, cellIndex++, column.getName(), column.getEnumType(),
                    column.getWidth(), column.getDateFormat(), column.getMultiple(), column.getAlignment());

            if (writeColumn != null) {
                if (valueFormatMap != null) {
                    ValueFormatter format = valueFormatMap.get(field);
                    if (format != null) {
                        writeColumn.setValueFormatter(format);
                    }
                }

                if (cellStyleCreatorMap != null) {
                    CellStyleCreator cellStyleCreator = cellStyleCreatorMap.get(field);
                    if (cellStyleCreator != null) {
                        writeColumn.setCellStyleCreator(cellStyleCreator);
                    }
                }

                writeColumns.add(writeColumn);
            }
        }

        if (writeColumns.size() == 0) {
            throw new BusinessException("需要导出的Excel列为空");
        }

        return new SimpleWriteRow(writeColumns);
    }
}
