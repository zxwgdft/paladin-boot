package com.paladin.framework.exception;

/**
 * 系统异常</br>
 * 系统异常记录了系统自身存在的，性质较严重的异常，
 * 是系统运行的一种风险，每一种系统异常应该有对应
 * 的应对说明，并且通过CODE对应
 *
 * @author TontZhou
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 7388296648211140775L;

    /**
     * 系统异
     */
    private int errorCode;

    public SystemException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public SystemException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
