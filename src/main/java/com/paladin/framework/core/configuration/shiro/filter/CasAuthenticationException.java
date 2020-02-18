package com.paladin.framework.core.configuration.shiro.filter;

import org.apache.shiro.authc.AuthenticationException;

public class CasAuthenticationException extends AuthenticationException{

	private static final long serialVersionUID = 7375416764682005374L;

	public CasAuthenticationException(String message) {
        super(message);
    }

    public CasAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
