package com.paladin.common.core.permission;

import com.paladin.common.core.CommonUserSession;
import com.paladin.framework.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Set;

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
            CommonUserSession userSession = CommonUserSession.getCurrentUserSession();
            Set<String> codes = userSession.getPermissionCodes();
            if (!codes.contains(permissionCode)) {
                throw new BusinessException("没有访问或操作权限");
            }
        }
    }

    @Before("@annotation(needAdmin)")
    public void beforeRequest(JoinPoint point, NeedAdmin needAdmin) {
        CommonUserSession userSession = CommonUserSession.getCurrentUserSession();
        if (!userSession.isSystemAdmin()) {
            throw new BusinessException("没有访问或操作权限");
        }
    }
}
