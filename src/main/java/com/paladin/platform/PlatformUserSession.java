package com.paladin.platform;

import com.paladin.common.specific.CommonUserSession;

/**
 * @author TontoZhou
 * @since 2020/1/19
 */
public class PlatformUserSession extends CommonUserSession {

    public PlatformUserSession(String userId, String userName, String account, String[] roleIds) {
        super(userId, userName, account, roleIds);
    }


}
