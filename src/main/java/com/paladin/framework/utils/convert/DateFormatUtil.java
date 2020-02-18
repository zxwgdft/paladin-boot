package com.paladin.framework.utils.convert;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateFormatUtil {

	private final static Map<String, ThreadLocal<SimpleDateFormat>> threadLocalMap = new HashMap<>();

	private static ThreadLocal<SimpleDateFormat> getThreadLocal(final String format) {

		ThreadLocal<SimpleDateFormat> threadLocal = threadLocalMap.get(format);
		
		if (threadLocal == null) {
			synchronized (threadLocalMap) {
				threadLocal = threadLocalMap.get(format);
				if (threadLocal == null) {
					threadLocal = new ThreadLocal<SimpleDateFormat>() {
						public SimpleDateFormat initialValue() {
							return new SimpleDateFormat(format);
						}
					};
					threadLocalMap.put(format, threadLocal);
				}
			}
		}

		return threadLocal;
		
	}

	/**
	 * 通过线程变量创建并获取安全的{@link SimpleDateFormat}
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getThreadSafeFormat(String format) {
		return getThreadLocal(format).get();
	}

}
