package com.paladin.common.core.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

import com.paladin.common.model.org.OrgPermission;
import com.paladin.common.model.org.OrgRole;
import com.paladin.framework.common.BaseModel;

public class Role {

	private String id;

	// 角色名称
	private String roleName;

	// 角色等级
	private int roleLevel;

	// 是否默认角色
	private boolean isDefault;

	// 是否启用
	private boolean enable;

	// 是否可授权
	private boolean grantable;

	// 角色说明
	private String roleDesc;

	private HashMap<String, MenuPermission> menuPermissionMap;
	private Collection<MenuPermission> rootMenuPermissionSet;
	private HashSet<String> permissionCodeSet;
	private List<OrgPermission> orgPermissions;
	private List<Permission> permissionObjects;

	public Role(OrgRole orgRole) {
		this.id = orgRole.getId();
		this.roleName = orgRole.getRoleName();
		this.roleLevel = orgRole.getRoleLevel();
		this.roleDesc = orgRole.getRoleDesc();
		this.isDefault = orgRole.getIsDefault() == BaseModel.BOOLEAN_YES;
		this.enable = orgRole.getEnable() == BaseModel.BOOLEAN_YES;

		this.menuPermissionMap = new HashMap<>();
		this.rootMenuPermissionSet = new HashSet<>();
		this.permissionCodeSet = new HashSet<>();
		this.orgPermissions = new ArrayList<>();
	}

	public Role() {
		this.menuPermissionMap = new HashMap<>();
		this.rootMenuPermissionSet = new HashSet<>();
		this.permissionCodeSet = new HashSet<>();
		this.orgPermissions = new ArrayList<>();
	}

	public void addPermission(OrgPermission orgPermission, Map<String, OrgPermission> allPermissionMap) {
		String code = orgPermission.getCode();
		if (code != null && code.length() > 0) {
			permissionCodeSet.add(code);
		}

		orgPermissions.add(orgPermission);

		String permissionId = orgPermission.getId();

		MenuPermission mp = menuPermissionMap.get(permissionId);
		if (mp == null) {
			mp = new MenuPermission(orgPermission, true);
			menuPermissionMap.put(permissionId, mp);

			String parentId = mp.getSource().getParentId();
			while (parentId != null && parentId.length() > 0) {
				if (menuPermissionMap.get(parentId) != null) {
					break;
				}

				OrgPermission op = allPermissionMap.get(parentId);
				if (op != null) {
					mp = new MenuPermission(op, false);
					menuPermissionMap.put(parentId, mp);
					parentId = op.getParentId();
				} else {
					break;
				}
			}
		} else {
			mp.setOwned(true);
		}
	}

	/**
	 * 初始化菜单权限，加入排序等
	 */
	public void initMenuPermission() {
		for (MenuPermission mp : menuPermissionMap.values()) {
			String parentId = mp.getSource().getParentId();
			if (parentId == null || parentId.length() == 0) {
				rootMenuPermissionSet.add(mp);
			} else {
				MenuPermission parentMp = menuPermissionMap.get(parentId);
				if (parentMp == null) {
					rootMenuPermissionSet.add(mp);
				} else {
					parentMp.addChild(mp);
				}
			}
		}

		ArrayList<MenuPermission> rootList = new ArrayList<>();
		for (MenuPermission mp : rootMenuPermissionSet) {
			mp.init();
			rootList.add(mp);
		}

		Collections.sort(rootList, new Comparator<MenuPermission>() {
			@Override
			public int compare(MenuPermission mr1, MenuPermission mr2) {
				return mr1.getListOrder() - mr2.getListOrder();
			}
		});

		this.rootMenuPermissionSet = rootList;

		List<MyPermission> permissionObjects = new ArrayList<>(orgPermissions.size());
		for (OrgPermission op : orgPermissions) {
			String code = op.getCode();
			if (code != null && code.length() > 0) {
				permissionObjects.add(new MyPermission(op.getId(), code));
			}
		}

		this.permissionObjects = Collections.unmodifiableList(permissionObjects);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<MenuPermission> getMenuPermissions() {
		return Collections.unmodifiableList((List) rootMenuPermissionSet);
	}

	public static Collection<MenuPermission> getMultiRoleMenuPermission(Collection<Role> roles) {

		HashSet<MenuPermission> root = new HashSet<>();
		HashMap<String, MenuPermission> mpMap = new HashMap<>();

		for (Role role : roles) {
			for (MenuPermission mp : role.menuPermissionMap.values()) {
				String id = mp.getSource().getId();
				MenuPermission a = mpMap.get(id);
				if (a != null) {
					if (a.isOwned() == false && mp.isOwned()) {
						a.setOwned(true);
					}
				} else {
					mpMap.put(id, new MenuPermission(mp.getSource(), mp.isOwned()));
				}
			}
		}

		for (MenuPermission mp : mpMap.values()) {
			String parentId = mp.getSource().getParentId();
			if (parentId == null || parentId.length() == 0) {
				root.add(mp);
			} else {
				MenuPermission parentMp = mpMap.get(parentId);
				if (parentMp == null) {
					root.add(mp);
				} else {
					parentMp.addChild(mp);
				}
			}
		}

		ArrayList<MenuPermission> rootList = new ArrayList<>();
		for (MenuPermission mp : root) {
			mp.init();
			rootList.add(mp);
		}

		Collections.sort(rootList, new Comparator<MenuPermission>() {
			@Override
			public int compare(MenuPermission mr1, MenuPermission mr2) {
				return mr1.getListOrder() - mr2.getListOrder();
			}
		});

		return rootList;
	}

	public static Collection<Permission> getMultiRolePermissionObject(Collection<Role> roles) {

		if (roles == null || roles.size() == 0) {
			return null;
		}

		HashMap<String, Permission> resultMap = new HashMap<>();
		for (Role role : roles) {
			List<Permission> permissionObjects = role.getPermissionObjects();
			for (Permission permissionObject : permissionObjects) {
				resultMap.put(((MyPermission) permissionObject).getId(), permissionObject);
			}
		}

		return resultMap.values();
	}

	public boolean ownPermission(String code) {
		return permissionCodeSet.contains(code);
	}

	public boolean ownLevel(int roleLevel) {
		return this.roleLevel >= roleLevel;
	}

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

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public boolean isGrantable() {
		return grantable;
	}

	public List<Permission> getPermissionObjects() {
		return permissionObjects;
	}

	public class MyPermission extends WildcardPermission {
		private static final long serialVersionUID = 2967736325743246351L;

		private String id;

		public MyPermission(String id, String code) {
			super(code);
			this.id = id;
		}

		public String getId() {
			return id;
		}

	}

}
