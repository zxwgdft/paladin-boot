package com.paladin.platform;

import com.paladin.common.model.syst.SysUser;
import com.paladin.common.service.syst.SysUserService;
import com.paladin.common.specific.CommonUserSession;
import com.paladin.common.utils.AESEncryptUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author TontoZhou
 * @since 2020/1/19
 */
@Component
public class PlatformUserRealm extends AuthorizingRealm {

    @Value("${platform.secret:bMFmZY9W1FRdkbqqAi9JWQ}")
    private String secret;

    @Autowired
    private SysUserService sysUserService;

    public PlatformUserRealm() {
        setAuthenticationTokenClass(PlatformUserToken.class);
        // 平台用户不需要凭证
        setCredentialsMatcher(new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                return true;
            }
        });
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken aToken) throws AuthenticationException {
        PlatformUserToken pToken = (PlatformUserToken) aToken;

        try {
            String userId = AESEncryptUtil.decrypt(pToken.getUserId(), secret);
            String token = AESEncryptUtil.decrypt(pToken.getToken(), secret);

            CommonUserSession userSession = new CommonUserSession(pToken.getUserId(), "平台用户", "平台用户");

            return new SimpleAuthenticationInfo(userSession, null, getName());
        } catch (Exception e) {
            throw new UnknownAccountException();
        }
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
