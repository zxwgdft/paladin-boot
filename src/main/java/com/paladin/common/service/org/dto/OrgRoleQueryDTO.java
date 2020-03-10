package com.paladin.common.service.org.dto;

import com.paladin.framework.service.OffsetPage;
import com.paladin.framework.service.QueryCondition;
import com.paladin.framework.service.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgRoleQueryDTO extends OffsetPage {

    @QueryCondition(type = QueryType.LIKE)
    private String roleName;

}