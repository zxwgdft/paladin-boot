package com.paladin.framework.excel.write;

/**
 * 
 * Excel读取异常
 * 
 * @author TontoZhou
 *
 */
public class ExcelWriteException extends Exception{
	
	private static final long serialVersionUID = -8556173589504368976L;

	public ExcelWriteException()
	{
		super();
	}
	
	public ExcelWriteException(String msg)
	{
		super(msg);
	}
	
	public ExcelWriteException(String msg, Throwable t)
	{
		super(msg, t);
	}
	
}
