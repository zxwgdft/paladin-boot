package com.paladin.common.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.paladin.common.core.exception.CommonHandlerExceptionResolver;
import com.paladin.common.core.log.OperationLogInterceptor;
import com.paladin.common.core.permission.PermissionMethodInterceptor;
import com.paladin.common.core.template.TontoDialect;
import com.paladin.framework.service.DataContainerManager;
import com.paladin.framework.service.QueryHandlerInterceptor;
import com.paladin.framework.service.QueryMethodInterceptor;
import com.paladin.framework.service.ServiceSupportManager;
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

    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
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
     * 数据容器管理器
     *
     * @return
     */
    @Bean
    public DataContainerManager getDataContainerManager() {
        return new DataContainerManager();
    }

    /**
     * service支持管理器
     *
     * @return
     */
    @Bean
    public ServiceSupportManager getServiceSupportConatiner() {
        return new ServiceSupportManager();
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

    /**
     * 自定义判断权限AOP
     *
     * @return
     */
    @Bean
    public PermissionMethodInterceptor getPermissionMethodInterceptor() {
        return new PermissionMethodInterceptor();
    }

    /**
     * 操作日志AOP
     *
     * @return
     */
    @Bean
    public OperationLogInterceptor getOperationLogInterceptor() {
        return new OperationLogInterceptor();
    }
}
