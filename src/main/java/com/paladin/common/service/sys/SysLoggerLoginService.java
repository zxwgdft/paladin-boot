package com.paladin.common.service.sys;

import com.paladin.common.model.sys.SysLoggerLogin;
import com.paladin.framework.service.CommonOrderBy;
import com.paladin.framework.service.OrderType;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

@Service
@CommonOrderBy(property = SysLoggerLogin.FIELD_CREATE_TIME, type = OrderType.DESC)
public class SysLoggerLoginService extends ServiceSupport<SysLoggerLogin> {

}