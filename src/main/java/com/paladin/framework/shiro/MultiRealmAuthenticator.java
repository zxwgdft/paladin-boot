package com.paladin.framework.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * 多Realm验证
 *
 * @author TontoZhou
 * @since 2019/11/27
 */
@Slf4j
public class MultiRealmAuthenticator extends ModularRealmAuthenticator {

    /**
     * 重写该方法保证异常正确抛出,需要多个Realm支持不同Token，否则会出现异常覆盖
     */
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {

        AuthenticationStrategy strategy = getAuthenticationStrategy();

        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        if (log.isTraceEnabled()) {
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        AuthenticationException authException = null;

        for (Realm realm : realms) {

            aggregate = strategy.beforeAttempt(realm, token, aggregate);

            if (realm.supports(token)) {

                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

                AuthenticationInfo info = null;

                try {
                    info = realm.getAuthenticationInfo(token);
                } catch (Throwable throwable) {

                    // 记录异常
                    if (throwable instanceof AuthenticationException) {
                        authException = (AuthenticationException) throwable;
                    } else {
                        authException = new AuthenticationException("账号登录异常", throwable);
                    }

                    if (log.isDebugEnabled()) {
                        String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
                        log.debug(msg, throwable);
                    }
                }

                aggregate = strategy.afterAttempt(realm, token, info, aggregate, authException);

            } else {
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }

        // 存在异常直接抛出
        if (authException != null) {
            throw authException;
        }

        aggregate = strategy.afterAllAttempts(token, aggregate);

        return aggregate;
    }
}
