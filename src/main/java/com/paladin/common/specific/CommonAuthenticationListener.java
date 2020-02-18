package com.paladin.common.specific;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.paladin.framework.utils.IPUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.paladin.common.model.syst.SysLoggerLogin;
import com.paladin.common.service.syst.SysLoggerLoginService;
import com.paladin.common.service.syst.SysUserService;
import com.paladin.framework.core.session.UserSession;
import com.paladin.framework.utils.uuid.UUIDUtil;

public class CommonAuthenticationListener implements AuthenticationListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SysLoggerLoginService sysLoggerLoginService;
	@Autowired
	private SysUserService sysUserService;

	@Override
	public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
		Object principal = info.getPrincipals().getPrimaryPrincipal();

		if (principal instanceof UserSession) {
			UserSession userSession = (UserSession) principal;

			String ip = null;
			if (token instanceof HostAuthenticationToken) {
				ip = ((HostAuthenticationToken) token).getHost();
			} else {
				// 获取request ip
				WebSubject webSubject = (WebSubject) SecurityUtils.getSubject();
				HttpServletRequest request = (HttpServletRequest) webSubject.getServletRequest();
				ip = IPUtil.getIpAddress(request);
			}
			
			String account = userSession.getAccount();

			SysLoggerLogin model = new SysLoggerLogin();
			model.setId(UUIDUtil.createUUID());
			model.setIp(ip);
			model.setAccount(account);
			// 待扩展
			model.setUserType(1);
			// 待扩展
			model.setLoginType(1);
			model.setUserId(userSession.getUserId());
			model.setCreateTime(new Date());

			sysLoggerLoginService.save(model);
			sysUserService.updateLastTime(account);

			logger.info("===>用户[" + account + "]登录系统<===");
		} else {
			logger.error("未知异常登录方式：" + token);
		}
	}

	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException ae) {

	}

	@Override
	public void onLogout(PrincipalCollection principals) {
		logger.info("===>用户[" + principals.getPrimaryPrincipal() + "]登出系统<===");
	}

}
