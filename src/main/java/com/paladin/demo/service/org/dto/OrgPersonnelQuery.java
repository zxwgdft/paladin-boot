package com.paladin.demo.service.org.dto;

import com.paladin.framework.service.OffsetPage;
import com.paladin.framework.service.QueryCondition;
import com.paladin.framework.service.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgPersonnelQuery extends OffsetPage {

    @QueryCondition(type = QueryType.LIKE)
    private String name;

    @QueryCondition(type = QueryType.EQUAL)
    private Integer identificationType;

    private String unitId;

}