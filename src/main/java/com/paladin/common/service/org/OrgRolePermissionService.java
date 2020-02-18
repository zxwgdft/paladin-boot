package com.paladin.common.service.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paladin.common.core.permission.PermissionContainer;
import com.paladin.common.mapper.org.OrgRolePermissionMapper;
import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.framework.core.ServiceSupport;

@Service
public class OrgRolePermissionService extends ServiceSupport<OrgRolePermission> {

	@Autowired
	private OrgRolePermissionMapper orgRolePermissionMapper;

	public List<String> getPermissionByRole(String id) {
		return orgRolePermissionMapper.getPermissionByRole(id);
	}

	@Transactional
	public boolean grantAuthorization(String roleId, String[] permissionIds) {
		if (roleId == null || roleId.length() == 0) {
			return false;
		}

		orgRolePermissionMapper.removePermissionByRole(roleId);
		if (permissionIds != null && permissionIds.length > 0) {
			orgRolePermissionMapper.insertByBatch(roleId, permissionIds);
		}

		PermissionContainer.updateData();
		return true;
	}

}