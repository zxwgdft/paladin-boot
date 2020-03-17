package com.paladin.common.core;

import com.paladin.common.model.sys.SysUser;
import com.paladin.framework.service.UserSession;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
public interface UserSessionFactory {

    UserSession createUserSession(SysUser sysUser);

}
