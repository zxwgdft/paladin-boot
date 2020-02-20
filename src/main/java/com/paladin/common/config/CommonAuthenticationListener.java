package com.paladin.common.config;

import com.paladin.common.model.syst.SysLoggerLogin;
import com.paladin.common.service.syst.SysLoggerLoginService;
import com.paladin.common.service.syst.SysUserService;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.utils.IPUtil;
import com.paladin.framework.utils.uuid.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
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
