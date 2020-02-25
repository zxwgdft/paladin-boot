package com.paladin.framework.excel;

import java.util.Date;

public interface ICell {
    Date getDate() throws ConvertException;

    Date getDate(String pattern) throws ConvertException;

    String getString() throws ConvertException;

    Integer getInteger() throws ConvertException;

    Boolean getBoolean() throws ConvertException;

    Double getDouble() throws ConvertException;

    Long getLong() throws ConvertException;
}
