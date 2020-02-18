package com.paladin.common.service.org.dto;

import com.paladin.framework.common.OffsetPage;
import com.paladin.framework.common.QueryCondition;
import com.paladin.framework.common.QueryType;

public class OrgRoleQueryDTO extends OffsetPage {

	private String roleName;

	@QueryCondition(type = QueryType.LIKE)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}