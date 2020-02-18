package com.paladin.platform;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author TontoZhou
 * @since 2020/1/19
 */
public class PlatformUserToken implements AuthenticationToken {

    private String userId;
    private String token;

    public PlatformUserToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
