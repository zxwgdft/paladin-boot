package com.paladin.common.core.security;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Collection;

/**
 * 简单权限的实现
 * <p>
 * NeedPermission:判断用户的权限code集合中是否存在需要的权限code
 * NeedAdmin:判断用户是否系统管理员
 * <p>
 * 拓展：可尝试获取URL来判断是否有权限，这样就可以不用硬编码权限code部分
 *
 * @author TontoZhou
 * @since 2020/3/19
 */
@Aspect
public class PermissionMethodInterceptor {

    @Before("@annotation(needPermission)")
    public void beforeRequest(JoinPoint point, NeedPermission needPermission) {
        String permissionCode = needPermission.value();
        if (permissionCode != null && permissionCode.length() > 0) {
            if (!PermissionUtil.hasPermission(permissionCode)) {
                throw new BusinessException("没有访问或操作权限");
            }
        }
    }

    @Before("@annotation(needAdmin)")
    public void beforeRequest(JoinPoint point, NeedAdmin needAdmin) {
        UserSession userSession = UserSession.getCurrentUserSession();
        if (!userSession.isSystemAdmin()) {
            throw new BusinessException("没有访问或操作权限");
        }
    }

    @Before("@annotation(needRole)")
    public void beforeRequest(JoinPoint point, NeedRole needRole) {
        UserSession userSession = UserSession.getCurrentUserSession();
        Collection<String> roles = userSession.getRoles();
        if (roles != null && roles.size() > 0) {
            String need = needRole.value();
            for (String role : roles) {
                if (StringUtil.equals(role, need)) {
                    return;
                }
            }
        }
        throw new BusinessException("没有访问或操作权限");
    }

    @Before("@annotation(needRoleLevel)")
    public void beforeRequest(JoinPoint point, NeedRoleLevel needRoleLevel) {
        UserSession userSession = UserSession.getCurrentUserSession();
        if (userSession.getRoleLevel() < needRoleLevel.value()) {
            throw new BusinessException("没有访问或操作权限");
        }
    }
}
