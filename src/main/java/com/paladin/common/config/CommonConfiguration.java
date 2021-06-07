package com.paladin.common.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.paladin.common.core.log.OperationLogInterceptor;
import com.paladin.common.core.security.PermissionMethodInterceptor;
import com.paladin.common.core.template.TontoDialect;
import com.paladin.common.core.upload.BigFileUploaderContainer;
import com.paladin.common.service.core.FileStoreService;
import com.paladin.common.service.core.impl.DefaultFileStoreService;
import com.paladin.framework.cache.DataCacheManager;
import com.paladin.framework.cache.MemoryDataCacheManager;
import com.paladin.framework.io.TemporaryFileHelper;
import com.paladin.framework.service.QueryHandlerInterceptor;
import com.paladin.framework.service.QueryMethodInterceptor;
import com.paladin.framework.service.ServiceSupportManager;
import com.paladin.framework.service.mybatis.CommonSqlInjector;
import com.paladin.framework.spring.SpringBeanHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
     * shiro 模板方言
     *
     * @return
     */
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 数据缓存管理器
     */
    @Bean
    public DataCacheManager getDataCacheManager() {
        return new MemoryDataCacheManager();
    }


    /**
     * spring bean 获取帮助类
     */
    @Bean
    public SpringBeanHelper springBeanHolder() {
        return new SpringBeanHelper();
    }

    /**
     * 基于mybatis plus和业务封装的支持类管理启用
     */
    @Bean
    public ServiceSupportManager getServiceSupportManager() {
        return new ServiceSupportManager();
    }

    /**
     * 扩展mybatis plus 通用方法
     */
    @Bean
    public CommonSqlInjector getCommonSqlInjector() {
        return new CommonSqlInjector();
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


    /**
     * 文件存储服务
     *
     * @return
     */
    @Bean
    public FileStoreService getFileStoreService() {
        return new DefaultFileStoreService();
    }

    /**
     * 临时文件助手
     */
    @Bean
    public TemporaryFileHelper getTemporaryFileHelper(Environment env) {
        String basePath = env.getProperty("paladin.file.base-path");
        return new TemporaryFileHelper(basePath);
    }

    /**
     * 大文件上传
     */
    @Bean
    public BigFileUploaderContainer getBigFileUploaderContainer(Environment env) {
        String basePath = env.getProperty("paladin.file.base-path");
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
        basePath += "upload/";
        return new BigFileUploaderContainer(basePath);
    }


}
