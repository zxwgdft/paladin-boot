package com.paladin.framework.utils.convert;

import com.paladin.framework.utils.reflect.ReflectUtil;
import org.springframework.util.NumberUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 简单的转化值工具类
 *
 * @author TontoZhou
 */
public class SimpleConvertUtil {


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
     * 解析字符串到目标类型（基本类型）
     *
     * @param str
     * @param type
     * @param dateFormat 日期格式
     * @return
     */
    public static Object parseString(String str, Class<?> type, String dateFormat) {
        if (str == null || str.length() == 0 || type == null) {
            return null;
        }

        if (type == String.class) {
            return str;
        } else if (type == Date.class) {
            try {
                long time = Long.parseLong(str);
                return new Date(time);
            } catch (Exception e) {
            }

            if (dateFormat != null && dateFormat.length() > 0) {
                try {
                    return DateFormatUtil.getThreadSafeFormat(dateFormat).parse(str);
                } catch (ParseException e) {
                }
            } else {
                try {
                    return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd HH:mm:ss").parse(str);
                } catch (ParseException e) {
                    try {
                        return DateFormatUtil.getThreadSafeFormat("yyyy-MM-dd").parse(str);
                    } catch (ParseException e1) {
                    }
                }
            }
        } else if (Class.class == type) {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
            }
        } else {
            Class<?> newType = ReflectUtil.getPackagePrimitive(type);
            if (newType != null) {
                type = newType;
            }

            if (Number.class.isAssignableFrom(type)) {
                return NumberUtils.parseNumber(str, (Class<? extends Number>) type);
            } else if (Boolean.class == type) {
                return Boolean.parseBoolean(str);
            } else if (Character.class == type) {
                return str.charAt(0);
            }
        }

        throw new IllegalArgumentException(
                "Cannot convert String [" + str + "] to target class [" + type.getName() + "]");
    }

}
