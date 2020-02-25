package com.paladin.common.core;

import com.paladin.common.model.sys.SysLoggerLogin;
import com.paladin.common.service.sys.SysLoggerLoginService;
import com.paladin.common.service.sys.SysUserService;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class CommonAuthenticationListener implements AuthenticationListener {

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
                ip = WebUtil.getIpAddress(request);
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

            log.info("===>用户[" + account + "]登录系统<===");
        } else {
            log.error("未知异常登录方式：" + token);
        }
    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {

    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        log.info("===>用户[" + principals.getPrimaryPrincipal() + "]登出系统<===");
    }

}
