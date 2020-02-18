package com.paladin.common.core.export;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.framework.excel.write.ValueFormator;

public class ExportCondition {

	public final static String FILE_TYPE_EXCEL = "excel";

	public final static String SCOPE_ALL = "all";
	public final static String SCOPE_PAGE = "page";

	private String fileType;
	private String dataScope;
	private List<ExportColumn> columns;
	
	@JsonIgnore
	private Map<String, ValueFormator> excelValueFormatMap;
	

	/**
	 * 按照list顺序设置cell序号
	 */
	public void sortCellIndex() {
		if (columns != null) {
			for (int i = 0; i < columns.size(); i++) {
				columns.get(i).setIndex(i);
			}
		}
	}

	public boolean isExportPage() {
		return SCOPE_PAGE.equals(dataScope);
	}

	public boolean isExportAll() {
		return SCOPE_ALL.equals(dataScope);
	}
	
	public Map<String, ValueFormator> getExcelValueFormatMap() {
		return excelValueFormatMap;
	}

	public void setExcelValueFormat(Map<String, ValueFormator> excelValueFormatMap) {
		this.excelValueFormatMap = excelValueFormatMap;
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public List<ExportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ExportColumn> columns) {
		this.columns = columns;
	}

	public static class ExportColumn {
		private String field;
		private String name;
		private Integer index;
		private String dateFormat;
		private String enumType;
		private Boolean multiple;
		private Integer width;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public String getDateFormat() {
			return dateFormat;
		}

		public void setDateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
		}

		public String getEnumType() {
			return enumType;
		}

		public void setEnumType(String enumType) {
			this.enumType = enumType;
		}

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public Boolean getMultiple() {
			return multiple;
		}

		public void setMultiple(Boolean multiple) {
			this.multiple = multiple;
		}

	}



}
