package com.paladin.framework.excel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import com.paladin.framework.utils.DateFormatUtil;

public class DefaultCell implements ICell {

	private Cell cell;

	public DefaultCell(Cell cell) {
		this.cell = cell;
	}

	@Override
	public Date getDate() throws ConvertException {
		if (cell == null) {
			throw new NullPointerException();
		}

		int type = cell.getCellType();

		if (type == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		}

		if (type == Cell.CELL_TYPE_STRING) {
			String str = cell.getStringCellValue();

			if (str == null) {
				return null;
			}
			str = str.trim();

			if (str.length() == 0) {
				return null;
			}

			try {
				return DateFormatUtil.getThreadSafeFormat("yyyy/MM/dd").parse(str);
			} catch (ParseException e) {
				try {
					return DateFormatUtil.getThreadSafeFormat("yyyy/MM/dd  HH:mm:ss").parse(str);
				} catch (ParseException e1) {
					throw new ConvertException("转化为时间失败");
				}
			}
		}

		return null;
	}

	@Override
	public Date getDate(String pattern) throws ConvertException {
		if (cell == null) {
			throw new NullPointerException();
		}

		int type = cell.getCellType();

		if (type == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		}

		if (type == Cell.CELL_TYPE_STRING) {
			String str = cell.getStringCellValue();

			if (str == null) {
				return null;
			}
			str = str.trim();

			if (str.length() == 0) {
				return null;
			}

			try {
				return DateFormatUtil.getThreadSafeFormat(pattern).parse(str);
			} catch (ParseException e) {
				throw new ConvertException("转化为时间失败");
			}
		}

		return null;
	}

	@Override
	public String getString() {
		if (cell == null) {
			throw new NullPointerException();
		}

		String result = null;
		int type = cell.getCellType();
		switch (type) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case Cell.CELL_TYPE_STRING: {
			result = cell.getStringCellValue();
			break;
		}
		case Cell.CELL_TYPE_NUMERIC: {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
					result = DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd").format(date);
				} else {
					result = null;
				}
			} else {
				result = new BigDecimal(cell.getNumericCellValue()).toString();
			}
			break;
		}
		case Cell.CELL_TYPE_FORMULA: {
			result = cell.getStringCellValue();
			if (result == null || result.length() == 0) {
				result = new BigDecimal(cell.getNumericCellValue()).toString();
			}
			break;
		}
		default: {
			result = null;
		}
		}

		if (result == null) {
			return null;
		}
		result = result.trim();
		return result.equals("") ? null : result;
	}

	@Override
	public Integer getInteger() throws ConvertException {
		if (cell == null) {
			throw new NullPointerException();
		}

		int type = cell.getCellType();

		if (type == Cell.CELL_TYPE_NUMERIC) {
			return (int) Math.round(cell.getNumericCellValue());
		} else if (type == Cell.CELL_TYPE_STRING) {
			String str = cell.getStringCellValue();
			if (str != null) {
				str = str.trim();
				if (str.length() != 0) {
					try {
						return Integer.valueOf(str);
					} catch (Exception e) {
						throw new ConvertException("转化为整数失败");
					}
				}
			}
		}

		return null;
	}

	@Override
	public Boolean getBoolean() throws ConvertException {
		if (cell == null) {
			throw new NullPointerException();
		}

		int type = cell.getCellType();

		if (type == Cell.CELL_TYPE_BOOLEAN) {
			return cell.getBooleanCellValue();
		} else if (type == Cell.CELL_TYPE_STRING) {
			String str = cell.getStringCellValue();
			if (str == null) {
				return null;
			}

			str = str.trim();
			if (str.length() == 0) {
				return null;
			}

			if ("是".equals(str) || "Y".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str)) {
				return true;
			}
			if ("否".equals(str) || "N".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
				return false;
			}

			throw new ConvertException("转化为布尔失败");
		} else if (type == Cell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue() > 0;
		}

		return null;
	}

	@Override
	public Double getDouble() throws ConvertException {
		if (cell == null) {
			throw new NullPointerException();
		}

		int type = cell.getCellType();

		if (type == Cell.CELL_TYPE_BLANK) {
			return null;
		}

		if (type == Cell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue();
		} else if (type == Cell.CELL_TYPE_STRING) {
			String str = cell.getStringCellValue();
			if (str != null) {
				str = str.trim();
				if (str.length() != 0) {
					try {
						return Double.valueOf(str);
					} catch (Exception e) {
						throw new ConvertException("转化为数字失败");
					}
				}
			}
		}

		return null;
	}

	@Override
	public Long getLong() throws ConvertException {
		if (cell == null) {
			throw new NullPointerException();
		}

		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_NUMERIC) {
			return Math.round(cell.getNumericCellValue());
		} else if (type == Cell.CELL_TYPE_STRING) {
			String str = cell.getStringCellValue();
			if (str != null) {
				str = str.trim();
				if (str.length() != 0) {
					try {
						return Long.valueOf(str);
					} catch (Exception e) {
						throw new ConvertException("转化为整数失败");
					}
				}
			}
		}
		return null;
	}

}
