package com.paladin.framework.excel;

import com.paladin.framework.utils.convert.DateFormatUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

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

        CellType type = cell.getCellType();

        if (type == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }

        if (type == CellType.STRING) {
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

        CellType type = cell.getCellType();

        if (type == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }

        if (type == CellType.STRING) {
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
        CellType type = cell.getCellType();

        if (type == CellType.BOOLEAN) {
            return cell.getBooleanCellValue() ? "true" : "false";
        } else if (type == CellType.STRING) {
            result = cell.getStringCellValue();
        } else if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                if (date != null) {
                    result = DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd").format(date);
                } else {
                    result = null;
                }
            } else {
                result = BigDecimal.valueOf(cell.getNumericCellValue()).toString();
            }
        } else if (type == CellType.FORMULA) {
            result = cell.getStringCellValue();
            if (result == null || result.length() == 0) {
                result = BigDecimal.valueOf(cell.getNumericCellValue()).toString();
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

        CellType type = cell.getCellType();
        if (type == CellType.NUMERIC) {
            return (int) Math.round(cell.getNumericCellValue());
        } else if (type == CellType.STRING) {
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

        CellType type = cell.getCellType();

        if (type == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (type == CellType.STRING) {
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
        } else if (type == CellType.NUMERIC) {
            return cell.getNumericCellValue() > 0;
        }

        return null;
    }

    @Override
    public Double getDouble() throws ConvertException {
        if (cell == null) {
            throw new NullPointerException();
        }

        CellType type = cell.getCellType();

        if (type == CellType.BLANK) {
            return null;
        }

        if (type == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (type == CellType.STRING) {
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

        CellType type = cell.getCellType();
        if (type == CellType.NUMERIC) {
            return Math.round(cell.getNumericCellValue());
        } else if (type == CellType.STRING) {
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
