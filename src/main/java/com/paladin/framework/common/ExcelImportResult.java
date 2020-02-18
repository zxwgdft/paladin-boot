package com.paladin.framework.common;

import java.util.List;

import com.paladin.framework.excel.read.CellValueException;

public class ExcelImportResult {

	private int successCount;
	private int errorCount;

	private List<ExcelImportError> errors;

	public ExcelImportResult(int totalCount, List<ExcelImportError> errors) {

		if (errors != null) {
			errorCount = errors.size();
		}

		successCount = totalCount - errorCount;

		this.errors = errors;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public List<ExcelImportError> getErrors() {
		return errors;
	}

	public static class ExcelImportError {

		private int index;

		private String message;

		public ExcelImportError(int index, String message) {
			this.index = index;
			this.message = message;
		}

		public ExcelImportError(int index, Exception e) {
			this.index = index;
			this.message = (e instanceof CellValueException) ? ((CellValueException) e).getReason() : e.getMessage();
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
