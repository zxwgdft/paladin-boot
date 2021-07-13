package com.paladin.common.service.sys.dto;

import com.paladin.framework.service.PageParam;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.annotation.QueryCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysLoggerLoginQuery extends PageParam {

    @QueryCondition(type = QueryType.EQUAL)
    private String account;

    @QueryCondition(type = QueryType.GREAT_EQUAL, name = "createTime")
    private Date startTime;

    @QueryCondition(type = QueryType.LESS_EQUAL, name = "createTime", nextDay = true)
    private Date endTime;

}