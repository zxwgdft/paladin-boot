package com.paladin.framework.core.exception;

/**
 * 系统异常
 * @author TontZhou
 *
 */
public class SystemException extends RuntimeException{
	
	private static final long serialVersionUID = 7388296648211140775L;

	private int errorCode;
	
	
	public SystemException(int errorCode) {
		super();
		this.errorCode= errorCode;
	}

	public SystemException(String message,int errorCode) {
		super(message);
		this.errorCode= errorCode;
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}
	
	public SystemException(String message) {
		super(message);
	}

	/**  
	 * errorCode  
	 */
	int getErrorCode() {
		return errorCode;
	}

	/**  
	 * errorCode  
	 */
	void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	
	public String getMessage() {
		return "系统异常[代码：" + errorCode + "]";
	}
	
}
