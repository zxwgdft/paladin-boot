package com.paladin.common.config;

import com.paladin.common.core.CommonAuthenticationListener;
import com.paladin.common.core.CommonCasUserRealm;
import com.paladin.common.core.CommonUserRealm;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 登录相关bean注入，主要由于shiro提前加载bean问题，因而把shiro相关bean单独拎出来注入
 */
@Configuration
public class AuthConfiguration {

    /**
     * 启用单点登录Realm
     *
     * @return
     */
    @Bean("casRealm")
    @ConditionalOnProperty(prefix = "paladin", value = "shiro-cas-enabled", havingValue = "true", matchIfMissing = false)
    public Pac4jRealm getCasRealm() {
        CommonCasUserRealm realm = new CommonCasUserRealm();
        realm.setAuthenticationTokenClass(Pac4jToken.class);
        return realm;
    }

    /**
     * 启用默认本地登录Realm
     *
     * @return
     */
    @Bean("localRealm")
    public AuthorizingRealm getLocalRealm() {
        CommonUserRealm realm = new CommonUserRealm();
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，当于 m比如散列两次，相d5("");
        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        realm.setAuthenticationTokenClass(UsernamePasswordToken.class);
        return realm;
    }

    /**
     * 登录登出验证监听
     *
     * @return
     */
    @Bean
    public CommonAuthenticationListener getCommonAuthenticationListener() {
        return new CommonAuthenticationListener();
    }

}
