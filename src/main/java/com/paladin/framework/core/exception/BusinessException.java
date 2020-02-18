package com.paladin.framework.core.exception;

/**
 * 业务异常
 * @author TontZhou
 *
 */
public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = -8265596521676533679L;
	
	
	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
