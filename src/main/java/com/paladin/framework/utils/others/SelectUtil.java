package com.paladin.framework.utils.others;

import com.paladin.framework.utils.reflect.NameUtil;
import com.paladin.framework.utils.reflect.ReflectUtil;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;

public class SelectUtil {

    public static String getSelectSql(Class<?> clazz, String prefix, boolean newline) {

        if (clazz != null) {
            StringBuilder sb = new StringBuilder();

            while (clazz != null || clazz == Object.class) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {

                    if (!ReflectUtil.isStatic(field) && field.getAnnotation(Transient.class) == null) {
                        String property = null;

                        Column columnAnno = field.getAnnotation(Column.class);
                        if (columnAnno != null) {
                            if (columnAnno.name().length() != 0) {
                                property = columnAnno.name();
                            }
                        }

                        if (property == null) {
                            property = field.getName();
                        }

                        String column = NameUtil.hump2underline(property);
                        if (prefix != null)
                            sb.append(prefix).append(".");
                        sb.append(column).append(" AS ").append(property).append(",");
                        if (newline) {
                            sb.append("\n");
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }

            if (sb.length() > 0) {
                return sb.deleteCharAt(sb.length() - 1).toString();
            }
        }

        return "";
    }

    public static void main(String[] args) {

    }
}
