package com.paladin.framework.utils.convert;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过线程变量提高效率，使用jmh测试结果几乎快100倍
 * <p>
 * 10线程 5轮测试结果如下（测试时只获取了两种format，如果format多了以后可能会稍微影响map命中率，但影响较小）
 * Benchmark             Mode  Cnt          Score         Error  Units
 * thread local方式  thrpt    5  386642994.134 ± 4473165.392  ops/s
 * new Object方式    thrpt    5    3391447.593 ±  327200.206  ops/s
 */
public class DateFormatUtil {

	private final static Map<String, ThreadLocal<SimpleDateFormat>> threadLocalMap = new ConcurrentHashMap<>();

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
