package com.paladin.common.config;

import com.paladin.framework.shiro.CommonShiroFilterFactoryBean;
import com.paladin.framework.shiro.MultiRealmAuthenticator;
import com.paladin.framework.shiro.ShiroProperties;
import com.paladin.framework.shiro.filter.*;
import com.paladin.framework.shiro.session.PaladinWebSessionManager;
import com.paladin.framework.shiro.session.ShiroRedisSessionDAO;
import io.buji.pac4j.context.ShiroSessionStore;
import io.buji.pac4j.engine.ShiroCallbackLogic;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.J2EContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2>shiro配置</h2>
 * <p>
 * 修改了部分shiro的代码，从而提高效率，减少session的重复读取
 * </p>
 *
 * @author TontoZhou
 * @since 2018年3月21日
 */
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class CommonShiroConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "paladin.shiro", value = "redis-enabled", havingValue = "true", matchIfMissing = false)
    public SessionDAO redisSessionDAO(ShiroProperties shiroProperties, RedisTemplate<String, Object> jdkRedisTemplate) {
        return new ShiroRedisSessionDAO(shiroProperties, jdkRedisTemplate);
    }

    @Bean(name = "sessionManager")
    public WebSessionManager webSessionManager(ShiroProperties shiroProperties, SessionFactory sessionFactory, @Autowired(required = false) SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new PaladinWebSessionManager(shiroProperties);

        if (shiroProperties.isRedisEnabled()) {
            // 如果设置集群共享session，需要redis来存放session
            sessionManager.setSessionDAO(sessionDAO);
            // 用户权限，认证等缓存设置，因为验证权限部分用其他方式实现，所以不需要缓存
            sessionManager.setSessionFactory(sessionFactory);

            // 是否开启检测，默认开启，session过期策略交给redis
            sessionManager.setSessionValidationSchedulerEnabled(false);
            // 是否删除无效的，默认开启，使用redis后不需要
            sessionManager.setDeleteInvalidSessions(false);
            // 是否在url上显示检索得到的sessionid，前后端分离使用token后不需要
            sessionManager.setSessionIdUrlRewritingEnabled(false);
        } else {
            // session 验证间隔
            sessionManager.setSessionValidationInterval(shiroProperties.getSessionValidationInterval() * 60 * 1000);
            // 3600000 milliseconds = 1 hour
            sessionManager.setGlobalSessionTimeout(shiroProperties.getSessionTime() * 60 * 1000);
            // 是否在url上显示检索得到的sessionid
            sessionManager.setSessionIdUrlRewritingEnabled(false);
        }

        // session 监听
        // Collection<SessionListener> sessionListeners = new ArrayList<>();
        // sessionListeners.add(new CustomSessionListener());
        // sessionManager.setSessionListeners(sessionListeners);

        return sessionManager;
    }

    @Bean(name = "securityManager")
    public WebSecurityManager webSecurityManage(ShiroProperties shiroProperties, WebSessionManager webSessionManager, List<Realm> realms,
                                                List<AuthenticationListener> authenticationListeners) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(new MultiRealmAuthenticator());
        securityManager.setRealms(realms);

        if (shiroProperties.isCasEnabled()) {
            securityManager.setSubjectFactory(new Pac4jSubjectFactory()); // cas
        }

        // 这是shiro提供的验证成功失败接口，如果在filter中处理登录成功失败不一定能覆盖所有情况
        Authenticator authenticator = securityManager.getAuthenticator();
        if (authenticator instanceof AbstractAuthenticator) {
            ((AbstractAuthenticator) authenticator).setAuthenticationListeners(authenticationListeners);
        }

        // 注入缓存管理器;
        // securityManager.setCacheManager(redisCacheManager());
        securityManager.setSessionManager(webSessionManager);
        return securityManager;
    }

    @Bean(name = "shiroFilter")
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shirFilter(WebSecurityManager webSecurityManager, ShiroProperties shiroProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new CommonShiroFilterFactoryBean(shiroProperties);

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(webSecurityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        // 增加自定义过滤
        Map<String, Filter> filters = new HashMap<>();

        PaladinFormAuthenticationFilter authenticationFilter = new PaladinFormAuthenticationFilter();
        filters.put("authc", authenticationFilter);
        filters.put("logout", new PaladinLogoutFilter());

        shiroFilterFactoryBean.setFilters(filters);
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // anon（匿名） org.apache.shiro.web.filter.authc.AnonymousFilter
        // authc（身份验证） org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        // authcBasic（http基本验证）org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
        // logout（退出） org.apache.shiro.web.filter.authc.LogoutFilter
        // noSessionCreation org.apache.shiro.web.filter.session.NoSessionCreationFilter
        // perms(许可验证) org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter
        // port（端口验证） org.apache.shiro.web.filter.authz.PortFilter
        // rest (rest方面) org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter

        filterChainDefinitionMap.put(shiroProperties.getLogoutUrl(), "logout");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "shiroFilter")
    @ConditionalOnProperty(prefix = "paladin.shiro", value = "cas-enabled", havingValue = "true", matchIfMissing = false)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager, ShiroProperties shiroProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new CommonShiroFilterFactoryBean(shiroProperties);
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        // 增加自定义过滤
        Map<String, Filter> filters = new HashMap<>();
        Config config = getConfig(shiroProperties);

        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(config);
        callbackFilter.setDefaultUrl(shiroProperties.getSuccessUrl());
        ShiroCallbackLogic<Object, J2EContext> callbackLogic = new PaladinShiroCallbackLogic<>(shiroProperties);
        callbackFilter.setCallbackLogic(callbackLogic);
        filters.put("callback", callbackFilter);

        PaladinCasLogoutFilter logoutFilter = new PaladinCasLogoutFilter(shiroProperties, config);
        filters.put("logout", logoutFilter);

        PaladinCasAuthenticationFilter authenticationFilter = new PaladinCasAuthenticationFilter(shiroProperties, config);
        filters.put("authc", authenticationFilter);

        shiroFilterFactoryBean.setFilters(filters);
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put(shiroProperties.getCasFilterUrlPattern(), "callback");
        filterChainDefinitionMap.put(shiroProperties.getLogoutUrl(), "logout");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    private Config getConfig(ShiroProperties cas) {
        CasConfiguration casConfiguration = new CasConfiguration(cas.getCasServerLoginUrl(), cas.getCasServerUrl() + "/");
        casConfiguration.setAcceptAnyProxy(true);
        casConfiguration.setProtocol(CasProtocol.valueOf(cas.getCasProtocol()));

        CasClient casClient = new CasClient(casConfiguration);
        casClient.setCallbackUrl(cas.getClientServerUrl() + cas.getCasFilterUrlPattern() + "?client_name=CasClient");

        Clients clients = new Clients(cas.getClientServerUrl() + cas.getCasFilterUrlPattern() + "?client_name=CasClient", casClient);
        Config config = new Config(clients);
        config.setSessionStore(new ShiroSessionStore());
        return config;
    }

    @Bean(name = "authenticationStrategy")
    public AuthenticationStrategy authenticationStrategy() {
        return new FirstSuccessfulStrategy();
    }

    @Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(WebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


}
