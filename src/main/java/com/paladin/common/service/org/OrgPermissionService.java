package com.paladin.common.service.org;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paladin.common.model.org.OrgPermission;
import com.paladin.framework.common.BaseModel;
import com.paladin.framework.common.Condition;
import com.paladin.framework.common.QueryType;
import com.paladin.framework.core.ServiceSupport;

@Service
public class OrgPermissionService extends ServiceSupport<OrgPermission> {

	public List<OrgPermission> findGrantablePermission() {
		return searchAll(new Condition(OrgPermission.COLUMN_FIELD_GRANTABLE, QueryType.EQUAL, BaseModel.BOOLEAN_YES));
	}

}