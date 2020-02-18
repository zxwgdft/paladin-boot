package com.paladin.common.core.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.paladin.common.core.TemporaryFileHelper;
import com.paladin.common.core.TemporaryFileHelper.TemporaryFileOutputStream;
import com.paladin.common.core.export.ExportCondition.ExportColumn;
import com.paladin.framework.core.exception.BusinessException;
import com.paladin.framework.excel.write.ExcelWriteException;
import com.paladin.framework.excel.write.ExcelWriter;
import com.paladin.framework.excel.write.ValueFormator;
import com.paladin.framework.excel.write.WriteColumn;

public class ExportUtil {

	public static <T> String export(ExportCondition condition, List<T> data, Class<T> exportClass) throws IOException, ExcelWriteException {

		String fileType = condition.getFileType();

		if (ExportCondition.FILE_TYPE_EXCEL.equals(fileType)) {
			try (TemporaryFileOutputStream output = TemporaryFileHelper.getFileOutputStream(null, "xlsx")) {
				exportExcel(condition, data, exportClass, output);
				return output.getFileRelativeUrl();
			}
		} else {
			throw new BusinessException("导出文件类型[" + fileType + "]不存在！");
		}

	}

	public static <T> void exportExcel(ExportCondition condition, List<T> data, Class<T> exportClass, OutputStream output)
			throws ExcelWriteException, IOException {

		List<ExportColumn> columns = condition.getColumns();
		if (columns == null || columns.size() == 0) {
			throw new BusinessException("需要导出的Excel列为空");
		}

		if (data == null || data.size() == 0) {
			throw new BusinessException("需要导出的数据为空");
		}

		List<WriteColumn> writeColumns = new ArrayList<WriteColumn>();
		Map<String, ValueFormator> valueFormatMap = condition.getExcelValueFormatMap();

		for (ExportColumn column : columns) {
			String field = column.getField();
			SimpleWriteColumn writeColumn = SimpleWriteColumn.newInstance(field, exportClass, column.getIndex(), column.getName(), column.getEnumType(),
					column.getWidth(), column.getDateFormat(), column.getMultiple());

			if (writeColumn != null) {
				if (valueFormatMap != null) {
					ValueFormator format = valueFormatMap.get(field);
					if (format != null) {
						writeColumn.setValueFormator(format);
					}
				}

				writeColumns.add(writeColumn);
			}
		}

		if (writeColumns.size() == 0) {
			throw new BusinessException("需要导出的Excel列为空");
		}

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		ExcelWriter<T> writer = new ExcelWriter<T>(workbook, new SimpleWriteRow(writeColumns));
		writer.openNewSheet("导出数据");
		writer.write(data);
		writer.output(output);
	}
}
