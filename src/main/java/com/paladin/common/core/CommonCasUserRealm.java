package com.paladin.common.core;

import com.paladin.common.model.sys.SysUser;
import com.paladin.common.service.sys.SysUserService;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class CommonCasUserRealm extends Pac4jRealm {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        final Pac4jToken token = (Pac4jToken) authenticationToken;
        final List<CommonProfile> profiles = token.getProfiles();
        final Pac4jPrincipal principal = new Pac4jPrincipal(profiles, getPrincipalNameAttribute());

        String username = principal.getName();
        SysUser sysUser = sysUserService.getUserByAccount(username);

        if (sysUser == null) {
            throw new UnknownAccountException("账号不存在");
        }

        if (sysUser.getState() != SysUser.STATE_ENABLED) {
            throw new LockedAccountException("账号被锁定不可用，请联系管理员"); // 帐号锁定
        }

        CommonUserSession userSession = new CommonUserSession(sysUser.getId(), username, username);
        List<Object> principals = Arrays.asList(userSession, principal);
        PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principalCollection, token.getCredentials());

        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException {
        // 废弃shiro缓存验证信息策略
        return null;
    }

    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            return null;
        }
        return (AuthorizationInfo) principals.getPrimaryPrincipal();
    }
}
