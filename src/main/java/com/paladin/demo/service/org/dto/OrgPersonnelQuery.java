package com.paladin.demo.service.org.dto;

import com.paladin.framework.service.PageParam;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.annotation.QueryCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgPersonnelQuery extends PageParam {

    @QueryCondition(type = QueryType.LIKE)
    private String name;

    @QueryCondition(type = QueryType.EQUAL)
    private Integer identificationType;

    private String unitId;

}