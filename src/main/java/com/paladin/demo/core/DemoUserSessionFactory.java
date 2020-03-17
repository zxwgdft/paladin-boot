package com.paladin.demo.core;

import com.paladin.common.core.UserSessionFactory;
import com.paladin.common.model.sys.SysUser;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.OrgPersonnelService;
import com.paladin.framework.service.UserSession;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Component
public class DemoUserSessionFactory implements UserSessionFactory {

    @Autowired
    private OrgPersonnelService personnelService;

    @Override
    public UserSession createUserSession(SysUser sysUser) {
        if (sysUser == null) {
            throw new UnknownAccountException("账号不存在");
        }

        int type = sysUser.getType();
        int state = sysUser.getState();

        if (state == SysUser.STATE_ENABLED) {
            if (type == SysUser.TYPE_ADMIN) {
                return new DemoUserSession(sysUser.getId(), "系统管理员", sysUser.getAccount());
            }

            if (type == SysUser.TYPE_USER) {
                String userId = sysUser.getUserId();
                OrgPersonnel personnel = personnelService.get(userId);
                if (personnel != null) {
                    String roleIds = personnel.getRoles();
                    if (roleIds != null && roleIds.length() > 0) {
                        return new DemoUserSession(userId, personnel.getName(), sysUser.getAccount(), personnel.getUnitId(), roleIds.split(","));
                    }
                }
            }
        }

        if (state == SysUser.STATE_DISABLED) {
            throw new DisabledAccountException();
        }

        throw new AuthenticationException("账号异常");
    }

}
