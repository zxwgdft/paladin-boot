package com.paladin.common.config;

import com.paladin.common.core.CommonCasUserRealm;
import com.paladin.common.core.CommonUserRealm;
import com.paladin.common.core.container.DefaultVersionContainerDAO;
import com.paladin.common.core.exception.CommonHandlerExceptionResolver;
import com.paladin.common.core.template.TontoDialect;
import com.paladin.framework.service.VersionContainerDAO;
import io.buji.pac4j.realm.Pac4jRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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
    @ConditionalOnProperty(prefix = "paladin", value = "cas-enabled", havingValue = "true", matchIfMissing = false)
    public Pac4jRealm getCasRealm() {
        return new CommonCasUserRealm();
    }

    /**
     * 启用默认本地登录Realm
     *
     * @return
     */
    @Bean("localRealm")
    public AuthorizingRealm getLocalRealm(Environment env) {
        CommonUserRealm realm = new CommonUserRealm();

        String failLimit = env.getProperty("paladin.login.failLimit");
        HashedCredentialsMatcher hashedCredentialsMatcher;
        if ("true".equals(failLimit)) {
            // 该处代码用于应付安全测试工具，扩展后可用于生产
            hashedCredentialsMatcher = new IPLoginFailLimiter();
        } else {
            hashedCredentialsMatcher = new HashedCredentialsMatcher();
        }

        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，当于 m比如散列两次，相d5("");
        realm.setCredentialsMatcher(hashedCredentialsMatcher);


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

    @Bean
    public VersionContainerDAO getVersionContainerDAO() {
        return new DefaultVersionContainerDAO();
    }

    // 需要跨域修改该代码并注入spring
    public FilterRegistrationBean<CorsFilter> filterRegistrationBean() {
        // 对响应头进行CORS授权
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
        corsConfiguration.setMaxAge(3600L);// 跨域过期时间 秒

        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }


}
