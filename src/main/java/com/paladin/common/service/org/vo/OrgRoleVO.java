package com.paladin.common.service.org.vo;

public class OrgRoleVO {

	// id
	private String id;

	// 角色名称
	private String roleName;

	// 角色等级（考核权限等级）
	private Integer roleLevel;

	// 角色说明
	private String roleDesc;

	// 是否默认角色（1是0否）
	private Integer isDefault;

	// 是否启用 1是0否
	private Integer enable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}