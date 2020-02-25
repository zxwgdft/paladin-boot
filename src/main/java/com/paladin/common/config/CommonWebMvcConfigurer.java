package com.paladin.common.config;

import com.paladin.common.utils.upload.BigFileUploaderContainer;
import com.paladin.framework.io.TemporaryFileHelper;
import com.paladin.framework.service.QueryHandlerInterceptor;
import com.paladin.framework.web.convert.DateFormatter;
import com.paladin.framework.web.convert.Integer2EnumConverterFactory;
import com.paladin.framework.web.convert.String2EnumConverterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "paladin", value = "web-enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CommonWebProperties.class)
public class CommonWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private CommonWebProperties webProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = webProperties.getFilePath();
        String staticPath = webProperties.getStaticPath();
        String faviconPath = webProperties.getFaviconPath();

        registry.addResourceHandler("/static/**").addResourceLocations(staticPath);
        registry.addResourceHandler("/file/**").addResourceLocations(filePath);
        registry.addResourceHandler("/favicon.ico").addResourceLocations(faviconPath);

        log.info("文件资源存放地址：" + filePath);
        log.info("静态资源存放地址：" + staticPath);
        log.info("favicon存放地址：" + faviconPath);

        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        String rootView = webProperties.getRootView();
        if (rootView != null && rootView.length() > 0) {
            registry.addViewController("/").setViewName(rootView);
        }
    }

    public void addInterceptors(InterceptorRegistry registry) {
        // 注册查询缓存拦截器
        registry.addInterceptor(new QueryHandlerInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter());
        registry.addConverterFactory(new Integer2EnumConverterFactory());
        registry.addConverterFactory(new String2EnumConverterFactory());
    }

    /**
     * 配置错误页面
     *
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/static/html/error_404.html"));
        factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/static/html/error_401.html"));
        factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/html/error_500.html"));
        return factory;
    }

    /**
     * 临时文件助手
     *
     * @param commonWebProperties
     * @return
     */
    @Bean
    public TemporaryFileHelper getTemporaryFileHelper(CommonWebProperties commonWebProperties) {
        return new TemporaryFileHelper(commonWebProperties.getFilePath());
    }

    /**
     * 大文件上传
     *
     * @param commonWebProperties
     * @return
     */
    @Bean
    public BigFileUploaderContainer getBigFileUploaderContainer(CommonWebProperties commonWebProperties) {
        return new BigFileUploaderContainer(commonWebProperties.getFilePath());
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
