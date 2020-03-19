package com.paladin.common.core.exception;

import com.paladin.framework.common.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

// 只能处理返回ResponseEntity，除非前后端分离，否则无法处理返回视图错误情况
@Slf4j
public class CommonResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> businessExceptionHandler(BusinessException ex, WebRequest request) {
        return handleExceptionInternal(ex, R.fail(ex.getMessage(), ex.getData()), new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> unauthorizedExceptionHandler(BusinessException ex, WebRequest request) {
        return handleExceptionInternal(ex, R.fail("没有权限访问"), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> systemExceptionHandler(SystemException ex, WebRequest request) {
        log.error("系统异常！", ex);
        return handleExceptionInternal(ex, R.fail(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> systemExceptionHandler(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, R.fail(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        log.error("发生异常", ex);

        if (body == null) {
            body = R.fail(ex.getMessage());
        }

        return new ResponseEntity<>(body, headers, status);
    }

}
