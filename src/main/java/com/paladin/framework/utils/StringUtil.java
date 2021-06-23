package com.paladin.framework.utils;

import java.lang.reflect.Array;
import java.util.*;

public class StringUtil {

    /**
     * 空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }


    /**
     * 字符串是否相等（相对直接String.equals更有效率）
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        if (s1.length() != s2.length()) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * 字符串是否相等（相对直接String.equalsIgnoreCase更有效率）
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        if (s1.length() != s2.length()) {
            return false;
        }
        return s1.equalsIgnoreCase(s2);
    }

    /**
     * 强力trim，可去除全角下的空格
     *
     * @param str
     * @return
     */
    public static String strongTrim(String str) {
        if (str == null) {
            return null;
        }

        char[] val = str.toCharArray();

        int len = val.length;
        int st = 0;

        while ((st < len) && (val[st] <= ' ' || val[st] == 12288)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == 12288)) {
            len--;
        }
        return ((st > 0) || (len < val.length)) ? str.substring(st, len) : str;
    }

    /**
     * 拼接数组
     *
     * @param separator
     * @param targets
     * @return
     */
    public static String join(char separator, Object... targets) {
        if (targets == null || targets.length == 0) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(targets[0].toString());
        for (int i = 1; i < targets.length; i++) {
            sb.append(separator);
            sb.append(targets[i].toString());
        }

        return sb.toString();
    }

    /**
     * 逗号拼接字符串
     *
     * @param target
     * @return
     */
    public static String join(final Iterable<?> target) {
        return join(target, ',');
    }

    /**
     * 分隔符拼接字符串
     */
    public static String join(final Iterable<?> target, final char separator) {
        if (target == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator<?> it = target.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(separator);
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

    /**
     * 转为字符串
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null)
            return "null";

        if (obj.getClass().isArray()) {
            int size = Array.getLength(obj);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++)
                sb.append(toString(Array.get(obj, i))).append(",");
            if (size > 0)
                sb.deleteCharAt(sb.length() - 1);
            return sb.append("]").toString();
        }

        return obj.toString();
    }

    /**
     * 拼接字符串转int数组
     */
    public static int[] stringToIntArray(String str) {
        return stringToIntArray(str, ",");
    }

    /**
     * 拼接字符串转int数组
     */
    public static int[] stringToIntArray(String str, String regex) {
        if (str == null || str.length() == 0) return new int[]{};
        String[] sArr = str.split(regex);
        int[] iArr = new int[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            String s = sArr[i];
            iArr[i] = Integer.valueOf(s);
        }
        return iArr;
    }


    /**
     * 拼接字符串转int集合
     */
    public static List<Integer> stringToIntList(String str) {
        return stringToIntList(str, ",");
    }

    /**
     * 拼接字符串转int的list集合
     */
    public static List<Integer> stringToIntList(String str, String regex) {
        if (str == null || str.length() == 0) return Collections.emptyList();
        String[] sArr = str.split(regex);
        List<Integer> list = new ArrayList<>(sArr.length);
        for (int i = 0; i < sArr.length; i++) {
            String s = sArr[i];
            list.add(Integer.valueOf(s));
        }
        return list;
    }

    /**
     * 拼接字符串转int的set集合
     */
    public static Set<Integer> stringToIntegerSet(String ids) {
        if (ids != null && ids.length() > 0) {
            String[] idArr = ids.split(",");
            Set<Integer> set = new HashSet<>(Math.max((int) (idArr.length / .75f) + 1, 16));
            for (String id : idArr) {
                set.add(Integer.valueOf(id));
            }
            return set;
        }
        return Collections.emptySet();
    }

}
