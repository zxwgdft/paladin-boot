package com.paladin.framework.excel.read;

/**
 * 
 * Excel读取异常
 * 
 * @author TontoZhou
 *
 */
public class ExcelReadException extends Exception{
	
	private static final long serialVersionUID = -8029596605560758371L;

	public ExcelReadException()
	{
		super();
	}
	
	public ExcelReadException(String msg)
	{
		super(msg);
	}
	
	public ExcelReadException(String msg, Throwable t)
	{
		super(msg, t);
	}

}
