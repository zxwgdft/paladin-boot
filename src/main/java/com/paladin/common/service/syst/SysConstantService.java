package com.paladin.common.service.syst;

import org.springframework.stereotype.Service;

import com.paladin.common.model.syst.SysConstant;
import com.paladin.framework.common.OrderType;
import com.paladin.framework.common.QueryOrderBy;
import com.paladin.framework.core.ServiceSupport;

@Service
@QueryOrderBy(property = { SysConstant.COLUMN_FIELD_TYPE, SysConstant.COLUMN_FIELD_ORDER_NO }, type = { OrderType.ASC, OrderType.ASC })
public class SysConstantService extends ServiceSupport<SysConstant> {

}