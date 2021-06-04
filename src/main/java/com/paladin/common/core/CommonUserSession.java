package com.paladin.common.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.Menu;
import com.paladin.common.core.security.MenuContainer;
import com.paladin.common.core.security.Role;
import com.paladin.common.core.security.RoleContainer;
import com.paladin.framework.service.UserSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.*;

/**
 * 通用用户会话信息
 *
 * @author TontoZhou
 * @since 2019年7月24日
 */
public class CommonUserSession extends UserSession implements AuthorizationInfo {


    /**
     * 非超级管理员初始化方法
     *
     * @param userId
     * @param userName
     * @param account
     * @param roleIds
     */
    public CommonUserSession(String userId, String userName, String account, String[] roleIds) {
        super(userId, userName, account);
        setRoleId(roleIds);
    }

    /**
     * 超级管理员初始化方法
     *
     * @param userId
     * @param userName
     * @param account
     */
    public CommonUserSession(String userId, String userName, String account) {
        super(userId, userName, account);
        this.roleLevel = 9999;
        this.isSystemAdmin = true;
    }

    /**
     * 获取当前用户会话
     *
     * @return
     */
    public static CommonUserSession getCurrentUserSession() {
        return (CommonUserSession) SecurityUtils.getSubject().getPrincipal();
    }

    protected List<String> roleIds;
    protected int roleLevel;
    protected boolean isSystemAdmin = false;

    /**
     * 设置角色ID
     *
     * @param roleIds
     */
    protected void setRoleId(String... roleIds) {
        List<String> roleIdList = new ArrayList<>(roleIds.length);
        int roleLevel = 0;
        RoleContainer roleContainer = DataCacheHelper.getData(RoleContainer.class);
        for (int i = 0; i < roleIds.length; i++) {
            String roleId = roleIds[i];
            if (roleId != null) {
                Role role = roleContainer.getRole(roleId);
                if (role != null) {
                    roleIdList.add(roleId);
                    roleLevel = Math.max(roleLevel, role.getLevel());
                }
            }
        }

        this.roleLevel = roleLevel;
        this.roleIds = roleIdList;
    }

    /**
     * 获取角色拥有的数据等级
     *
     * @return
     */
    public int getRoleLevel() {
        return roleLevel;
    }

    /**
     * 菜单资源
     *
     * @return
     */
    public Collection<Menu> getMenuResources() {
        MenuContainer menuContainer = DataCacheHelper.getData(MenuContainer.class);
        if (isSystemAdmin) {
            return menuContainer.getAdminMenus();
        }
        return menuContainer.getRoleMenus(roleIds);
    }

    @Override
    @JsonIgnore
    public Collection<String> getRoles() {
        return roleIds;
    }

    @Override
    @JsonIgnore
    public Collection<String> getStringPermissions() {
        // 返回权限字符串数组，这里返回null，如果
        return null;
    }

    @Override
    @JsonIgnore
    public Collection<Permission> getObjectPermissions() {
        if (isSystemAdmin) {
            return (List) RoleContainer.getSystemAdminRole().getPermissions();
        }

        if (roleIds.size() == 1) {
            Role role = RoleContainer.getRole(roleIds.get(0));
            if (role == null) {
                return null;
            } else {
                return (List) role.getPermissions();
            }
        }

        ArrayList<Role> roles = new ArrayList<>(roleIds.size());
        for (String rid : roleIds) {
            Role role = RoleContainer.getRole(rid);
            if (role != null) {
                roles.add(role);
            }
        }

        if (roles.size() == 0) {
            return null;
        }

        return (List) RoleContainer.getMultiRolePermission(roles);
    }

    /**
     * 获取所有权限的CODE集合，可用于简单方式判断权限
     */
    @JsonIgnore
    public Set<String> getPermissionCodes() {
        if (isSystemAdmin) {
            return RoleContainer.getSystemAdminRole().getPermissionCodes();
        }

        if (roleIds.size() == 1) {
            Role role = RoleContainer.getRole(roleIds.get(0));
            if (role == null) {
                return null;
            } else {
                return role.getPermissionCodes();
            }
        }


        Set<String> codeSet = new HashSet<>();
        for (String rid : roleIds) {
            Role role = RoleContainer.getRole(rid);
            if (role != null) {
                codeSet.addAll(role.getPermissionCodes());
            }
        }

        return codeSet;
    }


    @Override
    @JsonIgnore
    public Object getUserForView() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", getUserName());
        map.put("account", getAccount());
        map.put("roleLevel", getRoleLevel());
        map.put("isSystemAdmin", isSystemAdmin);
        return map;
    }

    @Override
    public boolean isSystemAdmin() {
        return isSystemAdmin;
    }

    public String toString() {
        return "用户名：" + getUserName() + "/账号：" + getAccount();
    }

}
