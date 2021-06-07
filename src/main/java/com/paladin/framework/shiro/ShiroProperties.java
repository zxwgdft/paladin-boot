package com.paladin.framework.shiro;

import com.paladin.framework.constants.GlobalProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
     * 短session过期时间（分钟）
     */
    private int sessionTime = 60;

    /**
     * 长session过期时间（分钟）
     */
    private int longSessionTime = 60 * 24 * 1;

    /**
     * session校验定时任务间隔（分钟）
     */
    private int sessionValidationInterval = 60;

    /**
     * session在只是更新时间变化情况下间隔多少分钟更新
     * 设置该时间可以减少序列化session更新到redis的次数（通过只延长原有session的过期时间），
     * 但也因此可能会出现提前结束session的情况，例如session过期时间为30分钟，更新间隔5分钟，
     * 可能出现5分钟没有更新+25分钟没有请求后session过期。
     * <p>
     * 结合实际业务设置
     */
    private int updateSessionInterval = 5;

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


    // ----------------------------
    //
    // CAS 部分参数
    //
    // ----------------------------

    private boolean casEnabled = false;

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
