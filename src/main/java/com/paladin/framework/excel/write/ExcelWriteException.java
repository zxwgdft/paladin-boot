package com.paladin.framework.excel.write;

/**
 * Excel读取异常
 *
 * @author TontoZhou
 */
public class ExcelWriteException extends Exception {

    public ExcelWriteException() {
        super();
    }

    public ExcelWriteException(String msg) {
        super(msg);
    }

    public ExcelWriteException(String msg, Throwable t) {
        super(msg, t);
    }

}
