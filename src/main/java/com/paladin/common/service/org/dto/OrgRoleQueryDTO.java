package com.paladin.common.service.org.dto;

import com.paladin.framework.service.PageParam;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.annotation.QueryCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgRoleQueryDTO extends PageParam {

    @QueryCondition(type = QueryType.LIKE)
    private String roleName;

}