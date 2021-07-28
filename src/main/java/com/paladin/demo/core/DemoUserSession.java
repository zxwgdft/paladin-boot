package com.paladin.demo.core;

import com.paladin.common.core.CommonUserSession;
import org.apache.shiro.SecurityUtils;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
public class DemoUserSession extends CommonUserSession {

    public static final int ROLE_LEVEL_AGENCY_ADMIN = 2;
    public static final int ROLE_LEVEL_PERSONNEL = 1;

    private Integer agencyId;

    public DemoUserSession(String userId, String userName, String account, boolean isSystemAdmin, Integer agencyId, int... roleIds) {
        super(userId, userName, account, isSystemAdmin, roleIds);
        this.agencyId = agencyId;
        if(isSystemAdmin) {
            this.roleLevel = ROLE_LEVEL_APP_ADMIN;
        }

    }

    public static DemoUserSession getCurrentUserSession() {
        return (DemoUserSession) SecurityUtils.getSubject().getPrincipal();
    }

    public Integer getAgencyId() {
        return agencyId;
    }
}
