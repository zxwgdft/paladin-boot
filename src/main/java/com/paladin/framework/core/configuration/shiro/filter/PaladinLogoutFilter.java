package com.paladin.framework.core.configuration.shiro.filter;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paladin.framework.utils.WebUtil;
import com.paladin.framework.web.response.CommonResponse;

public class PaladinLogoutFilter extends LogoutFilter {

	private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

		Subject subject = getSubject(request, response);

		// Check if POST only logout is enabled
		if (isPostOnlyLogout()) {

			// check if the current request's method is a POST, if not redirect
			if (!WebUtils.toHttp(request).getMethod().toUpperCase(Locale.ENGLISH).equals("POST")) {
				return onLogoutRequestNotAPost(request, response);
			}
		}

		String redirectUrl = getRedirectUrl(request, response, subject);
		// try/catch added for SHIRO-298:
		try {
			subject.logout();
		} catch (SessionException ise) {
			log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		}

		if (WebUtil.isAjaxRequest((HttpServletRequest) request)) {
			WebUtil.sendJsonByCors((HttpServletResponse) response, CommonResponse.getSuccessResponse("登出成功"));
		} else {
			issueRedirect(request, response, redirectUrl);
		}
		return false;
	}
}
