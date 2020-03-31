package com.paladin.framework.shiro;

import com.paladin.framework.GlobalProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "paladin.shiro")
public class ShiroProperties {

    /**
     * token field，如果NULL则不用
     */
    private String tokenField;

    /**
     * 登录方式session域名，区分本地登录和cas
     */
    private String loginTypeField = "_LOGIN_TYPE_FIELD";

    /**
     * session 保存在redis中key的前缀
     */
    private String sessionPrefix = "session_";

    /**
     * session 在redis中缓存时间
     */
    private int sessionTime = 30;

    /**
     * session lastAccessTime 更新间隔
     */
    private int accessTimeUpdateInterval = 120 * 1000;

    /**
     * 静态资源前缀，多个可用逗号分隔，如果没有则为空
     */
    private String staticResourcePrefix;

    /**
     * 需要验证资源前缀，如果为空，表示除静态资源外所有
     */
    private String authResourcePrefix = "/common/,/" + GlobalProperties.project + "/";

    /**
     * 登录URL
     */
    private String loginUrl = "/" + GlobalProperties.project + "/login";

    /**
     * 登出URL
     */
    private String logoutUrl = "/" + GlobalProperties.project + "/logout";

    /**
     * 登录成功跳转URL
     */
    private String successUrl = "/" + GlobalProperties.project + "/index";

    /**
     * 未验证跳转页面
     */
    private String unauthorizedUrl = "/static/html/error_401.html";

    /**
     * 是否启用redis
     */
    private boolean redisEnabled = false;

    /**
     * 请求头中Referers属性，用于简单处理CSRF攻击（应付评测）
     */
    private String referers = null;

    /**
     * shiro 过滤链定义
     */
    private Map<String, String> filterChainDefinition;

}
