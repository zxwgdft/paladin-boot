package com.paladin.common.core;

import com.paladin.common.model.sys.SysUser;
import com.paladin.common.service.sys.SysUserService;
import com.paladin.framework.service.UserSession;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonUserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserSessionFactory userSessionFactory;

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     * @param authToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;

        String username = token.getUsername();
        SysUser sysUser = sysUserService.getUserByAccount(username);

        if (sysUser == null) {
            throw new UnknownAccountException();
        }

        // 创建用户session，存储相关信息（角色，权限等）
        UserSession userSession = userSessionFactory.createUserSession(sysUser);

        // 加密方式交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        // usersession 对象会存入session属性中，与session保持同步，并在session中实现权限控制
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userSession, sysUser.getPassword(), ByteSource.Util.bytes(sysUser.getSalt()),
                getName());

        return authenticationInfo;
    }

    /**
     * 此方法调用 hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权): 1、如果用户正常退出，缓存自动清空； 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例， 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
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
