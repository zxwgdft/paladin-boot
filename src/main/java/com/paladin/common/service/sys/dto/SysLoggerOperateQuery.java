package com.paladin.common.service.sys.dto;

import com.paladin.framework.service.OffsetPage;
import com.paladin.framework.service.QueryCondition;
import com.paladin.framework.service.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLoggerOperateQuery extends OffsetPage {

    @QueryCondition(type=QueryType.LIKE)
    private String operateByName;

    @QueryCondition(type=QueryType.LIKE)
    private String modelName;

    @QueryCondition(type=QueryType.LIKE)
    private String operateName;

}