package com.paladin.common.service.sys.dto;

import com.paladin.framework.service.PageParam;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.annotation.QueryCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysLoggerOperateQuery extends PageParam {

    @QueryCondition(type = QueryType.EQUAL)
    private String operateByName;

    @QueryCondition(type = QueryType.EQUAL)
    private String modelName;

    @QueryCondition(type = QueryType.EQUAL)
    private String operateName;

    @QueryCondition(type = QueryType.GREAT_EQUAL, name = "operateTime")
    private Date startTime;

    @QueryCondition(type = QueryType.LESS_EQUAL, name = "operateTime", nextDay = true)
    private Date endTime;

}