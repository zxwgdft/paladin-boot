package com.paladin.framework.web.response;

public abstract class Response {

	public static final int STATUS_NO_LOGIN = -1;
	public static final int STATUS_NO_PERMISSION = -2;

	public static final int STATUS_ERROR = 0; // 系统异常
	public static final int STATUS_SUCCESS = 1; // 成功
	public static final int STATUS_FAIL = 2; // 失败
	public static final int STATUS_FAIL_VALID = 3; // 验证失败
}
