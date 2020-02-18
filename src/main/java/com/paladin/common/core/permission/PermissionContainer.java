package com.paladin.common.core.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paladin.common.model.org.OrgPermission;
import com.paladin.common.model.org.OrgRole;
import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.common.service.org.OrgPermissionService;
import com.paladin.common.service.org.OrgRolePermissionService;
import com.paladin.common.service.org.OrgRoleService;
import com.paladin.framework.common.BaseModel;
import com.paladin.framework.core.VersionContainer;
import com.paladin.framework.core.VersionContainerManager;

@Component
public class PermissionContainer implements VersionContainer {

	private static Logger logger = LoggerFactory.getLogger(PermissionContainer.class);

	@Autowired
	private OrgPermissionService orgPermissionService;

	@Autowired
	private OrgRoleService orgRoleService;

	@Autowired
	private OrgRolePermissionService orgRolePermissionService;

	private volatile Map<String, Role> roleMap;

	private volatile Map<String, OrgPermission> permissionMap;

	private volatile Role systemAdminRole;

	/**
	 * 初始化权限
	 */
	public void initPermission() {
		logger.info("------------初始化权限开始------------");

		Map<String, OrgPermission> permissionMap = new HashMap<>();
		List<OrgPermission> orgPermissions = orgPermissionService.findAll();
		for (OrgPermission orgPermission : orgPermissions) {
			permissionMap.put(orgPermission.getId(), orgPermission);
		}
		this.permissionMap = permissionMap;
		initRole();
		logger.info("------------初始化权限结束------------");
	}

	/**
	 * 初始化角色和角色授予的权限
	 */
	public void initRole() {
		logger.info("------------初始化角色开始------------");
		List<OrgRole> orgRoles = orgRoleService.findAll();
		Map<String, Role> roleMap = new HashMap<>();
		for (OrgRole orgRole : orgRoles) {
			Role role = new Role(orgRole);
			roleMap.put(role.getId(), role);
		}

		List<OrgRolePermission> orgRolePermissions = orgRolePermissionService.findAll();

		for (OrgRolePermission orgRolePermission : orgRolePermissions) {
			String roleId = orgRolePermission.getRoleId();
			String permissionId = orgRolePermission.getPermissionId();

			OrgPermission permission = permissionMap.get(permissionId);
			Role role = roleMap.get(roleId);
			role.addPermission(permission, permissionMap);
		}

		for (Role role : roleMap.values()) {
			role.initMenuPermission();
		}

		// 创建系统管理员角色及菜单
		Role systemAdminRole = new Role();
		for (OrgPermission orgPermission : permissionMap.values()) {
			if (orgPermission.getIsAdmin() == BaseModel.BOOLEAN_YES) {
				systemAdminRole.addPermission(orgPermission, permissionMap);
			}
		}
		systemAdminRole.initMenuPermission();

		this.systemAdminRole = systemAdminRole;
		this.roleMap = roleMap;
		logger.info("------------初始化权限结束------------");
	}

	/**
	 * 获取角色
	 * 
	 * @param id
	 * @return
	 */
	public Role getRole(String id) {
		return roleMap.get(id);
	}

	/**
	 * 获取系统管理员角色
	 * 
	 * @return
	 */
	public Role getSystemAdminRole() {
		return systemAdminRole;
	}
	
	/**
	 * 获取角色列表
	 * @return
	 */
	public List<Role> getRoles(){
		return new ArrayList<>(roleMap.values());
	}
	
	@Override
	public String getId() {
		return "permission_container";
	}

	private static PermissionContainer container;

	public static PermissionContainer getInstance() {
		return container;
	}
	
	public static void updateData() {
		VersionContainerManager.versionChanged(container.getId());
	}

	@Override
	public boolean versionChangedHandle(long version) {
		initPermission();
		container = this;
		return true;
	}

}
