package com.paladin.framework.excel;

/**
 * 
 * 转化异常
 * 
 * @author TontoZhou
 * 
 */
public class ConvertException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -554275216793484541L;

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(String message, Throwable t) {
		super(message, t);
	}

	public ConvertException(Object orgin, Class<?> target) {
		this("[" + orgin.getClass() + "]:" + orgin.toString() + " convert to [" + target + "] failed");
	}

	public ConvertException(Object orgin, Class<?> target, Throwable t) {
		this("[" + orgin.getClass() + "]:" + orgin.toString() + " convert to [" + target + "] failed", t);
	}

	public ConvertException(Object orgin, Class<?> target, String message) {
		this("[" + orgin.getClass() + "]:" + orgin.toString() + " convert to [" + target + "] failed ==> " + message);
	}

	public ConvertException(Object orgin, Class<?> target, String message, Throwable t) {
		this("[" + orgin.getClass() + "]:" + orgin.toString() + " convert to [" + target + "] failed ==> " + message, t);
	}
}
