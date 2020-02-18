package com.paladin.framework.utils.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import com.paladin.framework.utils.reflect.ReflectUtil;
import com.paladin.framework.utils.structure.SecHashMap;
import com.paladin.framework.utils.DateFormatUtil;

public class DataConvertManager {

	private SecHashMap<Class<?>, Class<?>, DataConverter> converterMap = new SecHashMap<>();

	private final static DataConverter toStringConverter = new DataConverter() {
		@Override
		public Object convert(Object value) {
			return value.toString();
		}
	};

	private static SecHashMap<Class<?>, Class<?>, DataConverter> baseConverterMap = new SecHashMap<>();

	private static class NumberConverter implements DataConverter {

		private Class<?> toType;

		private NumberConverter(Class<?> toType) {
			this.toType = toType;
		}

		@Override
		public Object convert(Object value) {

			Number num = (Number) value;

			if (toType == Double.class) {
				return num.doubleValue();
			} else if (toType == Integer.class) {
				return num.intValue();
			} else if (toType == Long.class) {
				return num.longValue();
			} else if (toType == Short.class) {
				return num.shortValue();
			} else if (toType == Float.class) {
				return num.floatValue();
			} else if (toType == Byte.class) {
				return num.byteValue();
			} else if (toType == BigDecimal.class) {
				return new BigDecimal(num.toString());
			} else if (toType == BigInteger.class) {
				return new BigInteger(num.toString());
			}

			return null;
		}

	}

	private static class StringConverter implements DataConverter {

		private Class<?> toType;

		private StringConverter(Class<?> toType) {
			this.toType = toType;
		}

		@Override
		public Object convert(Object value) {

			String str = (String) value;

			if (toType == Double.class) {
				return Double.parseDouble(str);
			} else if (toType == Integer.class) {
				return Integer.parseInt(str);
			} else if (toType == Long.class) {
				return Long.parseLong(str);
			} else if (toType == Short.class) {
				return Short.parseShort(str);
			} else if (toType == Float.class) {
				return Float.parseFloat(str);
			} else if (toType == Byte.class) {
				return Byte.parseByte(str);
			} else if (toType == Character.class) {
				return str.charAt(0);
			} else if (toType == BigDecimal.class) {
				return new BigDecimal(str);
			} else if (toType == BigInteger.class) {
				return new BigInteger(str);
			}

			return null;
		}
	}

	static {

		Class<?>[] numberTypes = new Class<?>[] { Double.class, Integer.class, Long.class, Short.class, Float.class, Byte.class,
				BigDecimal.class, BigInteger.class };

		for (Class<?> from : numberTypes)
			for (Class<?> to : numberTypes)
				baseConverterMap.put(from, to, new NumberConverter(to));

		for (Class<?> to : numberTypes)
			baseConverterMap.put(String.class, to, new StringConverter(to));

		baseConverterMap.put(String.class, Character.class, new StringConverter(Character.class));
		baseConverterMap.put(String.class, Date.class, new DataConverter() {

			@Override
			public Object convert(Object value) {

				String str = (String) value;
				try {
					return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd HH:mm:ss").parse(str);
				} catch (ParseException e) {
					try {
						return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd").parse(str);
					} catch (ParseException e1) {
						try {
							long time = Long.parseLong(str);
							return new Date(time);
						} catch (Exception e2) {

						}
					}
				}

				return null;
			}

		});

		baseConverterMap.put(Long.class, Date.class, new DataConverter() {

			@Override
			public Object convert(Object value) {
				Long time = (Long) value;
				return new Date(time);
			}

		});

		baseConverterMap.put(Long.class, Date.class, new DataConverter() {
			@Override
			public Object convert(Object value) {
				Long time = (Long) value;
				return new Date(time);
			}
		});

		baseConverterMap.put(Date.class, String.class, new DataConverter() {
			@Override
			public Object convert(Object value) {
				return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
			}
		});
	}

	public void putConverter(Class<?> from, Class<?> to, DataConverter converter) {
		converterMap.put(from, to, converter);
	}

	public DataConverter getConverter(Class<?> from, Class<?> to) {

		if (from.isPrimitive())
			from = ReflectUtil.getPackagePrimitive(from);

		if (to.isPrimitive())
			to = ReflectUtil.getPackagePrimitive(to);

		DataConverter converter = null;

		Class<?> fromC = from;
		while (fromC != null) {
			converter = converterMap.get(fromC, to);

			if (converter != null)
				break;

			fromC = fromC.getSuperclass();
		}

		if (converter == null && to == String.class)
			return toStringConverter;

		return converter;
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(Object value, Class<T> target) {

		if (value == null || target == null)
			return null;

		DataConverter converter = getConverter(value.getClass(), target);

		return converter == null ? null : (T) converter.convert(value);
	}

}
