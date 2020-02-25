package com.paladin.common.config;

import com.paladin.common.core.CommonCasUserRealm;
import com.paladin.common.core.CommonUserRealm;
import com.paladin.common.core.DefaultVersionContainerDAO;
import com.paladin.common.core.exception.CommonHandlerExceptionResolver;
import com.paladin.common.core.template.TontoDialect;
import com.paladin.common.core.CommonAuthenticationListener;
import com.paladin.framework.service.QueryHandlerInterceptor;
import com.paladin.framework.service.QueryMethodInterceptor;
import com.paladin.framework.service.VersionContainerDAO;
import io.buji.pac4j.realm.Pac4jRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class CommonConfiguration {

    /**
     * 启动模板自定义方言
     *
     * @return
     */
    @Bean
    public TontoDialect getTontoDialect() {
        return new TontoDialect();
    }


    /**
     * 启用单点登录Realm
     *
     * @return
     */
    @Bean("casRealm")
    @ConditionalOnProperty(prefix = "paladin", value = "shiro-cas-enabled", havingValue = "true", matchIfMissing = false)
    public Pac4jRealm getCasRealm() {
        return new CommonCasUserRealm();
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
        realm.setCredentialsMatcher(new HashedCredentialsMatcher());
        return realm;
    }

    /**
     * 启用异常统一处理
     *
     * @return
     */
    @Bean
    public HandlerExceptionResolver getHandlerExceptionResolver() {
        return new CommonHandlerExceptionResolver();
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

    /**
     * 版本容器持久化
     *
     * @return
     */
    @Bean
    public VersionContainerDAO getVersionContainerDAO() {
        return new DefaultVersionContainerDAO();
    }


    /**
     * 查询回显拦截器
     *
     * @return
     */
    @Bean
    public QueryHandlerInterceptor getQueryHandlerInterceptor() {
        return new QueryHandlerInterceptor();
    }

    /**
     * 查询回显方法AOP
     *
     * @return
     */
    @Bean
    public QueryMethodInterceptor getQueryMethodInterceptor() {
        return new QueryMethodInterceptor();
    }


}
