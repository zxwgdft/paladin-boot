package com.paladin.common.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.*;
import com.paladin.framework.service.UserSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;

import java.util.*;

/**
 * 通用用户会话信息
 *
 * @author TontoZhou
 * @since 2019年7月24日
 */
public class CommonUserSession extends UserSession implements AuthorizationInfo {


    public CommonUserSession(String userId, String userName, String account) {
        super(userId, userName, account);
    }

    /**
     * 获取当前用户会话
     *
     * @return
     */
    public static CommonUserSession getCurrentUserSession() {
        return (CommonUserSession) SecurityUtils.getSubject().getPrincipal();
    }

    protected List<Integer> roleIds;
    protected int roleLevel;
    protected boolean isSystemAdmin;

    /**
     * 设置角色ID
     *
     * @param roleIds
     */
    protected void setRoleId(Integer... roleIds) {
        Set<Integer> roleIdSet = new HashSet<>();
        int roleLevel = 0;
        RoleContainer roleContainer = DataCacheHelper.getData(RoleContainer.class);
        for (int i = 0; i < roleIds.length; i++) {
            Integer roleId = roleIds[i];
            if (roleId != null) {
                Role role = roleContainer.getRole(roleId);
                if (role != null) {
                    roleIdSet.add(roleId);
                    roleLevel = Math.max(roleLevel, role.getLevel());
                }
            }
        }

        this.roleLevel = roleLevel;
        this.roleIds = new ArrayList<>(roleIdSet);
    }

    /**
     * 获取角色拥有的数据等级
     */
    public int getRoleLevel() {
        return roleLevel;
    }

    /**
     * 获取角色ID列表
     */
    public List<Integer> getRoleIdList() {
        return roleIds;
    }

    @Override
    public boolean isSystemAdmin() {
        return isSystemAdmin;
    }

    /**
     * 菜单资源
     *
     * @return
     */
    public Collection<Menu> getMenuResources() {
        MenuContainer menuContainer = DataCacheHelper.getData(MenuContainer.class);
        if (menuContainer == null) return Collections.EMPTY_LIST;
        if (isSystemAdmin) {
            return menuContainer.getAdminMenus();
        }
        return menuContainer.getRoleMenus(roleIds);
    }

    @Override
    @JsonIgnore
    public Collection<String> getRoles() {
        throw new RuntimeException("不支持该方法，需要请重写");
    }

    @Override
    @JsonIgnore
    public Collection<String> getStringPermissions() {
        throw new RuntimeException("不支持该方法，需要请重写");
    }

    @Override
    @JsonIgnore
    public Collection getObjectPermissions() {
        PermissionContainer permissionContainer = DataCacheHelper.getData(PermissionContainer.class);
        if (permissionContainer == null) return Collections.EMPTY_LIST;
        if (isSystemAdmin) {
            return permissionContainer.getAdminPermission();
        }

        return permissionContainer.getPermissionByRole(roleIds);
    }

    /**
     * 获取所有权限的CODE集合，可用于简单方式判断权限
     */
    @JsonIgnore
    public Collection<String> getPermissionCodes() {
        PermissionContainer permissionContainer = DataCacheHelper.getData(PermissionContainer.class);
        if (permissionContainer == null) return Collections.EMPTY_LIST;
        if (isSystemAdmin) {
            return permissionContainer.getAdminPermissionCode();
        }
        return permissionContainer.getPermissionCodeByRole(roleIds);
    }


    public String toString() {
        return "用户名：" + getUserName() + "/账号：" + getAccount();
    }

}
