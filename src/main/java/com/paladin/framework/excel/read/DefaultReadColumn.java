package com.paladin.framework.excel.read;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.paladin.framework.excel.ConvertException;
import com.paladin.framework.excel.EnumContainer;
import com.paladin.framework.excel.ICell;
import com.paladin.framework.excel.PropertyValidate;
import com.paladin.framework.excel.Validate;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.reflect.Entity;
import com.paladin.framework.utils.reflect.EntityField;
import com.paladin.framework.utils.ValidateUtil;

/**
 * 缺省Excel读取列实例
 * 
 * @author TontoZhou
 * @since 2018年11月2日
 */
public class DefaultReadColumn extends ReadColumn {

	// 是否可为空
	private boolean nullable = true;
	// 正则校验值
	private Pattern pattern;
	// 如果为字符串的话，字符串最小长度，null不检查
	private Integer minLength;
	// 如果为字符串的话，字符串最大长度，null不检查
	private Integer maxLength;
	// 整数枚举
	private int[] intEnum;
	// 最大值
	private BigDecimal max;
	// 最小值
	private BigDecimal min;

	private boolean isEnum;
	private String enumType;

	private Class<?> type = String.class;

	private EntityField entityField;

	private EnumContainer enumContainer;

	@Override
	public String validateValue(Object value) {

		if (value == null) {
			if (!nullable)
				return "不能为空";
			else
				return null;
		}

		String str = value.toString();

		if (pattern != null && !pattern.matcher(str).matches())
			return "格式不正确";

		if (minLength != null && str.length() < minLength)
			return "值长度不能小于" + minLength;

		if (maxLength != null && str.length() > maxLength)
			return "值长度不能大于" + maxLength;

		if (min != null && !ValidateUtil.validLessNumber(min, true, value))
			return "值不能小于" + value;

		if (max != null && !ValidateUtil.validGreatNumber(max, true, value))
			return "值不能大于" + value;

		if (intEnum != null && intEnum.length > 0 && !ValidateUtil.validContainInt(value, intEnum))
			return "值必须在" + StringUtil.toString(intEnum) + "之中";

		return null;
	}

	@Override
	public Object convertValue(ICell cell) throws ExcelReadException {
		if (cell == null) {
			return null;
		}

		try {
			if (isEnum) {
				String name = cell.getString();
				if (name != null && name.length() > 0) {
					String key = enumContainer != null ? enumContainer.getEnumKey(enumType, name) : name;
					if (key == null) {
						throw new ExcelReadException("第" + (cellIndex + 1) + "列值[" + name + "]为无效值");
					}

					if (type == Integer.class) {
						try {
							return Integer.valueOf(key);
						} catch (Exception e) {
							throw new ExcelReadException("第" + (cellIndex + 1) + "列值[" + name + "]为非法值");
						}
					}

					return key;
				}
				return null;
			}

			Class<?> type = getType();

			if (type == String.class) {
				return cell.getString();
			}

			if (type == Integer.class) {
				return cell.getInteger();
			}

			if (type == Long.class) {
				return cell.getLong();
			}

			if (type == Float.class) {
				Double d = cell.getDouble();
				return d == null ? null : d.floatValue();
			}

			if (type == Double.class) {
				return cell.getDouble();
			}

			if (type == Date.class) {
				return cell.getDate();
			}

			if (type == Boolean.class) {
				return cell.getBoolean();
			}

			return cell.getString();
		} catch (ConvertException e) {
			throw new ExcelReadException(e.getMessage());
		}
	}

	@Override
	public void fillValue(Object object, Object value) {
		entityField.setValue(object, value);
	}

	private final static Map<Class<? extends ReadPropertyConvert<?>>, ReadPropertyConvert<?>> convert_cache = new HashMap<>();
	private final static Map<Class<? extends PropertyValidate>, PropertyValidate> validate_cache = new HashMap<>();

	/**
	 * 创建列，暂时不处理实例中带有子实例情况，即列中列情况
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<ReadColumn> createReadColumn(Class<?> clazz, EnumContainer enumContainer) {

		if (clazz == Object.class)
			return null;

		List<ReadColumn> columns = new ArrayList<>();
		Entity entity = Entity.getEntity(clazz);

		for (EntityField field : entity.getEntityFields()) {
			ReadProperty readProp = field.getAnnotation(ReadProperty.class);
			if (readProp != null) {

				Class<?> type = field.getType();

				DefaultReadColumn column = new DefaultReadColumn();
				String fieldname = field.getName();

				column.setId(fieldname);
				column.setType(type);
				column.setCellIndex(readProp.cellIndex());
				column.setEnumContainer(enumContainer);
				column.entityField = field;

				String enumType = readProp.enumType();
				if (enumType != null && enumType.length() > 0) {
					column.setEnum(true);
					column.setEnumType(enumType);
				}

				CellConvert convertAnno = field.getAnnotation(CellConvert.class);

				if (convertAnno != null) {
					Class<? extends ReadPropertyConvert<?>> convertType = convertAnno.convert();
					// 获取或者创建值转换类
					ReadPropertyConvert<?> convert = convert_cache.get(convertType);
					if (convert == null) {
						synchronized (convert_cache) {
							convert = convert_cache.get(convertType);
							if (convert == null) {
								try {
									convert = convertType.newInstance();
								} catch (InstantiationException | IllegalAccessException e) {
									throw new RuntimeException(e.getMessage(), e);
								}
								convert_cache.put(convertType, convert);
							}
						}
					}
					column.setConvert(convert);
				}

				Validate validateAnno = field.getAnnotation(Validate.class);

				if (validateAnno != null) {
					Class<? extends PropertyValidate> validateType = validateAnno.validate();
					// 获取验证
					PropertyValidate validator = validate_cache.get(validateType);
					if (validator == null) {
						synchronized (validate_cache) {
							validator = validate_cache.get(validateType);
							if (validator == null) {
								try {
									validator = validateType.newInstance();
								} catch (InstantiationException | IllegalAccessException e) {
									throw new RuntimeException(e.getMessage(), e);
								}
								validate_cache.put(validateType, validator);
							}
						}
					}

					column.setValidate(validator);
				}

				// 简易验证
				column.setNullable(readProp.nullable());

				if (readProp.maxLength() > 0)
					column.setMaxLength(readProp.maxLength());
				if (readProp.minLength() > 0)
					column.setMinLength(readProp.minLength());
				if (!"".equals(readProp.regex()))
					column.setPattern(Pattern.compile(readProp.regex()));
				if (!"".equals(readProp.min()))
					column.setMin(new BigDecimal(readProp.min()));
				if (!"".equals(readProp.max()))
					column.setMax(new BigDecimal(readProp.max()));

				column.setIntEnum(readProp.intEnum());

				columns.add(column);
			}
		}

		return columns;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public int[] getIntEnum() {
		return intEnum;
	}

	public void setIntEnum(int[] intEnum) {
		this.intEnum = intEnum;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public String getEnumType() {
		return enumType;
	}

	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}

	public EnumContainer getEnumContainer() {
		return enumContainer;
	}

	public void setEnumContainer(EnumContainer enumContainer) {
		this.enumContainer = enumContainer;
	}

}
