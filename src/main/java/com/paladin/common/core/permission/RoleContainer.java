package com.paladin.common.core.permission;

import com.paladin.common.model.org.OrgRole;
import com.paladin.common.model.org.OrgRolePermission;
import com.paladin.common.service.org.OrgRolePermissionService;
import com.paladin.common.service.org.OrgRoleService;
import com.paladin.framework.common.BaseModel;
import com.paladin.framework.service.VersionContainer;
import com.paladin.framework.service.VersionContainerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class RoleContainer implements VersionContainer {

    @Autowired
    private OrgRoleService orgRoleService;

    @Autowired
    private OrgRolePermissionService orgRolePermissionService;

    private static Map<String, Role> roleMap;
    private static List<Role> roleList;
    private static Role systemAdminRole;

    /**
     * 初始化角色和角色授予的权限
     */
    public void initRole() {
        log.info("------------初始化角色开始------------");

        List<OrgRolePermission> orgRolePermissions = orgRolePermissionService.findAll();
        List<OrgRole> orgRoles = orgRoleService.findAll();

        // 读取角色对应权限集合
        Map<String, List<Permission>> rolePermissionMap = new HashMap<>();
        for (OrgRolePermission orgRolePermission : orgRolePermissions) {
            String roleId = orgRolePermission.getRoleId();
            String permissionId = orgRolePermission.getPermissionId();

            Permission permission = PermissionContainer.getPermission(permissionId);
            if (permission != null) {
                List<Permission> rolePermissions = rolePermissionMap.get(roleId);
                if (rolePermissions == null) {
                    rolePermissions = new ArrayList<>();
                    rolePermissionMap.put(roleId, rolePermissions);
                }
                rolePermissions.add(permission);
            }
        }

        // 创建并初始化角色权限
        Map<String, Role> roleMap = new HashMap<>();
        List<Role> roleList = new ArrayList<>();
        for (OrgRole orgRole : orgRoles) {
            Role role = new Role(orgRole);
            roleMap.put(role.getId(), role);
            roleList.add(role);
        }

        for (Role role : roleMap.values()) {
            List<Permission> ownedPermissions = rolePermissionMap.get(role.getId());
            if (ownedPermissions == null) {
                ownedPermissions = new ArrayList<>();
            }
            role.setPermission(ownedPermissions, createMenu(ownedPermissions));
        }

        // 创建系统管理员角色及菜单
        Role systemAdminRole = new Role(null);
        List<Permission> ownedPermission = new ArrayList<>();
        for (Permission permission : PermissionContainer.getAllPermission()) {
            if (permission.getSource().getIsAdmin() == BaseModel.BOOLEAN_YES) {
                ownedPermission.add(permission);
            }
        }
        systemAdminRole.setPermission(ownedPermission, createMenu(ownedPermission));

        RoleContainer.systemAdminRole = systemAdminRole;
        RoleContainer.roleMap = Collections.unmodifiableMap(roleMap);
        RoleContainer.roleList = Collections.unmodifiableList(roleList);

        log.info("------------初始化权限结束------------");
    }


    /**
     * 初始化角色权限数据
     *
     * @param ownedPermission
     */
    private static List<Menu> createMenu(List<Permission> ownedPermission) {
        Map<String, Menu> menuMap = new HashMap<>();
        Map<String, Menu> rootMenu = new HashMap<>();

        if (ownedPermission != null) {
            for (Permission permission : ownedPermission) {
                if (permission.isMenu()) {
                    String pid = permission.getId();
                    Menu menu = menuMap.get(pid);
                    if (menu == null) {
                        menu = new Menu(permission, true);
                        menuMap.put(pid, menu);

                        Permission parent = permission.getParentMenuPermission();
                        while (parent != null) {
                            String parentId = parent.getId();
                            Menu parentMenu = menuMap.get(parentId);
                            if (parentMenu != null) {
                                parentMenu.getChildren().add(menu);
                                break;
                            } else {
                                parentMenu = new Menu(parent, false);
                                menuMap.put(parentId, parentMenu);
                                parentMenu.getChildren().add(menu);
                            }

                            menu = parentMenu;
                            parent = parent.getParentMenuPermission();
                        }

                        if (menu.isRoot()) {
                            rootMenu.put(menu.getId(), menu);
                        }
                    } else {
                        menu.setOwned(true);
                    }
                }
            }
        }
        return new ArrayList<>(rootMenu.values());
    }


    /**
     * 获取角色
     *
     * @param id
     * @return
     */
    public static Role getRole(String id) {
        return roleMap.get(id);
    }

    /**
     * 获取多个角色
     *
     * @param ids
     * @return
     */
    public static List<Role> getRoles(String[] ids) {
        if (ids == null || ids.length == 0) return null;
        List<Role> roles = new ArrayList<>(ids.length);
        for (String id : ids) {
            Role role = roleMap.get(id);
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }

    /**
     * 获取系统管理员角色
     *
     * @return
     */
    public static Role getSystemAdminRole() {
        return systemAdminRole;
    }

    /**
     * 获取所有角色列表
     *
     * @return
     */
    public static List<Role> getRoles() {
        return roleList;
    }


    /**
     * 获取多角色菜单
     *
     * @param roles
     * @return
     */
    public static List<Menu> getMultiRoleMenu(List<Role> roles) {
        if (roles == null || roles.size() == 0) return null;
        if (roles.size() == 1) return roles.get(0).getRootMenus();

        Map<String, Permission> permissionMap = new HashMap<>();

        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                permissionMap.put(permission.getId(), permission);
            }
        }

        return createMenu(new ArrayList<>(permissionMap.values()));
    }

    /**
     * 获取多角色权限
     *
     * @param roles
     * @return
     */
    public static List<Permission> getMultiRolePermission(List<Role> roles) {
        if (roles == null || roles.size() == 0) return null;
        if (roles.size() == 1) return roles.get(0).getPermissions();

        Map<String, Permission> permissionMap = new HashMap<>();

        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                permissionMap.put(permission.getId(), permission);
            }
        }

        return new ArrayList<>(permissionMap.values());
    }


    private final static String CONTAINER_ID = "role_container";

    private static RoleContainer container;

    public static void updateData() {
        VersionContainerManager.versionChanged(CONTAINER_ID);
    }

    // 需要在PermissionContainer之后启动
    public int order() {
        return 99;
    }

    @Override
    public String getId() {
        return CONTAINER_ID;
    }

    @Override
    public boolean versionChangedHandle(long version) {
        initRole();
        container = this;
        return true;
    }

}
