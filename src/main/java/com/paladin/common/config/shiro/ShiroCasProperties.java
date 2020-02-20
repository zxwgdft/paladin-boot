package com.paladin.common.config.shiro;

import com.paladin.framework.GlobalProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "paladin.shiro")
public class ShiroCasProperties extends ShiroProperties {

    /**
     * CAS 服务端URL
     */
    private String casServerUrl;

    /**
     * CAS 服务端登录URL
     */
    private String casServerLoginUrl;

    /**
     * 客户端URL
     */
    private String clientServerUrl;

    /**
     * CAS 客户端登录URL
     */
    private String clientLoginUrl = "/" + GlobalProperties.project + "/login/cas";

    /**
     * CASFilter
     */
    private String casFilterUrlPattern = "/" + GlobalProperties.project + "/cas";

    /**
     * CAS 协议
     */
    private String casProtocol = "CAS30";

    /**
     *
     */
    private String casErrorUrl = "/static/html/error_cas_500.html";


}
