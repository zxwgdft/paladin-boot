package com.paladin.demo.core;

import com.paladin.common.core.CommonUserSession;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Getter
@Setter
public class DemoUserSession extends CommonUserSession {

    public static final int ROLE_LEVEL_APP_ADMIN = 9;
    public static final int ROLE_LEVEL_AGENCY_ADMIN = 2;
    public static final int ROLE_LEVEL_PERSONNEL = 1;

    private String unitId;

    public DemoUserSession(String userId, String userName, String account) {
        super(userId, userName, account);
    }

    public static DemoUserSession getCurrentUserSession() {
        return (DemoUserSession) SecurityUtils.getSubject().getPrincipal();
    }
}
