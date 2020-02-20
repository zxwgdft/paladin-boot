package com.paladin.common.service.sys;

import com.paladin.common.model.sys.SysConstant;
import com.paladin.framework.service.OrderType;
import com.paladin.framework.service.QueryOrderBy;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

@Service
@QueryOrderBy(property = { SysConstant.COLUMN_FIELD_TYPE, SysConstant.COLUMN_FIELD_ORDER_NO }, type = { OrderType.ASC, OrderType.ASC })
public class SysConstantService extends ServiceSupport<SysConstant> {

}