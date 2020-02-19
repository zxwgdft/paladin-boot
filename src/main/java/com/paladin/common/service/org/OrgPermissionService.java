package com.paladin.common.service.org;

import com.paladin.common.model.org.OrgPermission;
import com.paladin.framework.common.BaseModel;
import com.paladin.framework.service.Condition;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrgPermissionService extends ServiceSupport<OrgPermission> {

	public List<OrgPermission> findGrantablePermission() {
		return searchAll(new Condition(OrgPermission.COLUMN_FIELD_GRANTABLE, QueryType.EQUAL, BaseModel.BOOLEAN_YES));
	}

}