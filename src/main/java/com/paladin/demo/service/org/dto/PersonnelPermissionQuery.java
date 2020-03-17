package com.paladin.demo.service.org.dto;

import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.framework.service.QueryCondition;
import com.paladin.framework.service.QueryType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/3/17
 */
@Getter
@Setter
public class PersonnelPermissionQuery {

    @QueryCondition(name = OrgPersonnel.FIELD_UNIT_ID, type = QueryType.IN)
    private List<String> unitIds;

    @QueryCondition(name = OrgPersonnel.FIELD_UNIT_ID, type = QueryType.EQUAL)
    private String unitId;

    @QueryCondition(type = QueryType.EQUAL)
    private String id;

}
