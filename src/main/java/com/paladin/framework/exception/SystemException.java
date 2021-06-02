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

    /**
     * 异常代码，请检查代码规则
     */
    public static final int CODE_ERROR_CODE = 10000;

    /**
     * 配置异常，请检查配置参数
     */
    public static final int CODE_ERROR_CONFIG = 20000;

    /**
     * 数据异常，请检查数据是否被人工修改
     */
    public static final int CODE_ERROR_DATA = 30000;


    /**
     * 系统异常码
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
