package com.paladin.framework.excel;

import java.util.Date;

public interface ICell {
	public Date getDate() throws ConvertException;
	public Date getDate(String pattern) throws ConvertException;
	public String getString() throws ConvertException;
	public Integer getInteger() throws ConvertException;
	public Boolean getBoolean() throws ConvertException;
	public Double getDouble() throws ConvertException;
	public Long getLong() throws ConvertException;
}
