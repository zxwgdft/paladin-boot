package com.paladin.common.model.org;

import javax.persistence.Id;

public class OrgRolePermission {

	// 角色ID
	@Id
	private String roleId;

	// 权限ID
	@Id
	private String permissionId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

}