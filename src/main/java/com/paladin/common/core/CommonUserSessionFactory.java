package com.paladin.common.core;

import com.paladin.common.model.sys.SysUser;
import com.paladin.framework.service.UserSession;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Component
@ConditionalOnMissingBean(UserSessionFactory.class)
public class CommonUserSessionFactory implements UserSessionFactory {

    @Override
    public UserSession createUserSession(SysUser sysUser) {
        if (sysUser == null) {
            throw new UnknownAccountException("账号不存在");
        }

        if (sysUser.getState() != SysUser.STATE_ENABLED) {
            throw new DisabledAccountException();
        }

        return new CommonUserSession(sysUser.getId(), sysUser.getAccount(), sysUser.getAccount());
    }

}
