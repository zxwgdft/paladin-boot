package com.paladin.framework.utils;

import java.lang.reflect.Array;
import java.util.Collection;

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
		return !isEmpty(str);
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
	 * 拼接成字符串
	 * @param coll
	 * @return
	 */
	public static String splitString(Collection<?> coll) {
		return splitString(coll, ",");
	}

	/**
	 * 拼接成字符串
	 * @param coll
	 * @param separator 分隔符
	 * @return
	 */
	public static String splitString(Collection<?> coll, String separator) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : coll) {
			sb.append(obj.toString()).append(separator);
		}
		return sb.toString();
	}
	
	/**
	 * 转为字符串
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
}
