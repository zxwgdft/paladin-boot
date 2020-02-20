package com.paladin.framework.shiro.filter;

import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.utils.WebUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class PaladinFormAuthenticationFilter extends FormAuthenticationFilter {

    public static final String ERROR_KEY_LOGIN_FAIL_MESSAGE = "loginFailMessage";

    private static final Logger log = LoggerFactory.getLogger(PaladinFormAuthenticationFilter.class);

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }

                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " + "Authentication url [" + getLoginUrl() + "]");
            }

            if (WebUtil.isAjaxRequest((HttpServletRequest) request)) {
                WebUtil.sendJsonByCors((HttpServletResponse) response, R.fail(HttpCode.UNAUTHORIZED, "未登录或会话超时"));
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }

            return false;
        }
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (WebUtil.isAjaxRequest((HttpServletRequest) request)) {
            WebUtil.sendJsonByCors((HttpServletResponse) response, R.success(UserSession.getCurrentUserSession().getUserForView()));
            return false;
        } else {
            issueSuccessRedirect(request, response);
            return false;
        }
    }


    private final static Map<Class, String> exceptionMessageMap;

    static {
        exceptionMessageMap = new HashMap<>();
        exceptionMessageMap.put(IncorrectCredentialsException.class, "账号密码不正确");
        exceptionMessageMap.put(ExpiredCredentialsException.class, "账号密码过期");
        exceptionMessageMap.put(CredentialsException.class, "账号密码异常");
        exceptionMessageMap.put(ConcurrentAccessException.class, "无法同时多个用户登录");
        exceptionMessageMap.put(UnknownAccountException.class, "账号不存在");
        exceptionMessageMap.put(ExcessiveAttemptsException.class, "账号验证次数超过限制");
        exceptionMessageMap.put(LockedAccountException.class, "账号被锁定");
        exceptionMessageMap.put(DisabledAccountException.class, "账号被禁用");
        exceptionMessageMap.put(AccountException.class, "账号异常");
        exceptionMessageMap.put(UnsupportedTokenException.class, "不支持当前TOKEN");
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        String errorMsg = exceptionMessageMap.get(e.getClass());
        if (errorMsg == null) {
            errorMsg = e.getMessage();
        }

        if (WebUtil.isAjaxRequest((HttpServletRequest) request)) {
            WebUtil.sendJsonByCors((HttpServletResponse) response, R.fail(HttpCode.UNAUTHORIZED, errorMsg));
            return false;
        } else {
            setFailureAttribute(request, e);
            request.setAttribute(ERROR_KEY_LOGIN_FAIL_MESSAGE, errorMsg);
            return true;
        }

    }

}
