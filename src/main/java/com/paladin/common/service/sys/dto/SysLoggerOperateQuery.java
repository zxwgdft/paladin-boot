package com.paladin.common.service.sys.dto;

import com.paladin.framework.service.PageParam;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.annotation.QueryCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLoggerOperateQuery extends PageParam {

    @QueryCondition(type=QueryType.LIKE)
    private String operateByName;

    @QueryCondition(type=QueryType.LIKE)
    private String modelName;

    @QueryCondition(type=QueryType.LIKE)
    private String operateName;

}