package com.paladin.framework.utils.convert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的转化值工具类
 *
 * @author TontoZhou
 */
public class SimpleConvertUtil {


    private static final Map<String, Class<?>> primitives = new HashMap<String, Class<?>>(8);

    static {
        primitives.put("byte", Byte.TYPE);
        primitives.put("char", Character.TYPE);
        primitives.put("double", Double.TYPE);
        primitives.put("float", Float.TYPE);
        primitives.put("int", Integer.TYPE);
        primitives.put("long", Long.TYPE);
        primitives.put("short", Short.TYPE);
        primitives.put("boolean", Boolean.TYPE);
    }

    /**
     * 转换字符串为目标类型（支持基本类型和日期）
     *
     * @param str
     * @param type
     * @return
     */
    public static Object parseString(String str, Class<?> type) {
        return parseString(str, type, null);
    }

    /**
     * @param str
     * @param type
     * @return
     */
    public static Object parseString(String str, Class<?> type, String dateFormat) {

        if (str == null || str.length() == 0 || type == null)
            return null;

        if (type == String.class) {
            return str;
        }

        if (type == Date.class) {
            try {
                long time = Long.parseLong(str);
                return new Date(time);
            } catch (Exception e) {

            }

            if (dateFormat != null && dateFormat.length() > 0) {
                try {
                    return DateFormatUtil.getThreadSafeFormat(dateFormat).parse(str);
                } catch (ParseException e) {
                    return null;
                }
            } else {
                try {
                    return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd HH:mm:ss").parse(str);
                } catch (ParseException e) {
                    try {
                        return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd").parse(str);
                    } catch (ParseException e1) {
                        return null;
                    }
                }
            }
        }

        try {
            Class<?> newType = primitives.get(type.getSimpleName());

            if (newType != null) {
                type = newType;
            }

            if (type == Double.class) {
                return Double.parseDouble(str);
            } else if (type == Integer.class) {
                return Integer.parseInt(str);
            } else if (type == Long.class) {
                return Long.parseLong(str);
            } else if (type == Boolean.class) {
                return Boolean.parseBoolean(str);
            } else if (type == Short.class) {
                return Short.parseShort(str);
            } else if (type == Float.class) {
                return Float.parseFloat(str);
            } else if (type == Character.class) {
                return str.charAt(0);
            } else if (type == BigDecimal.class) {
                return new BigDecimal(str);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }


}
