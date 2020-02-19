package com.paladin.framework.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *
 * @author TontZhou
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -8265596521676533679L;

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private Object data;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Object data, Throwable cause) {
        super(message, cause);
        this.data = data;
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(HttpStatus httpStatus, String message, Object data) {
        super(message);
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public BusinessException(HttpStatus httpStatus, String message, Object data, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
