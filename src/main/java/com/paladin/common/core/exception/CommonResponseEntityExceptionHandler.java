package com.paladin.common.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paladin.framework.core.exception.BusinessException;
import com.paladin.framework.core.exception.SystemException;
import com.paladin.framework.web.response.CommonResponse;

// 只能处理返回ResponseEntity，除非前后端分离，否则无法处理返回视图错误情况
public class CommonResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> businessExceptionHandler(BusinessException ex, WebRequest request) {
		return handleExceptionInternal(ex, CommonResponse.getFailResponse(ex.getMessage()), new HttpHeaders(), HttpStatus.OK, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> systemExceptionHandler(SystemException ex, WebRequest request) {
		return handleExceptionInternal(ex, CommonResponse.getErrorResponse(ex.getMessage()), new HttpHeaders(), HttpStatus.OK, request);
	}
}
