package com.paladin.common.core.security;

import java.util.*;

/**
 * @author TontoZhou
 * @since 2021/3/23
 */
public class PermissionContainer {

    // role to permission code set
    private Map<String, Set<String>> role2PermissionCodeMap;

    // role to permission set
    private Map<String, Set<Permission>> role2PermissionMap;

    // permission id to permission
    private Map<String, Permission> permissionMap;

    private List<Permission> adminPermissionList;
    private List<String> adminPermissionCodeList;

    public PermissionContainer(Map<String, Permission> permissionMap, Map<String, Set<String>> role2PermissionCodeMap, Map<String, Set<Permission>> role2PermissionMap) {
        this.role2PermissionCodeMap = role2PermissionCodeMap;
        this.role2PermissionMap = role2PermissionMap;
        this.permissionMap = permissionMap;

        this.adminPermissionList = new ArrayList<>(permissionMap.values());
        this.adminPermissionCodeList = new ArrayList<>(adminPermissionList.size());

        for (Permission permission : adminPermissionList) adminPermissionCodeList.add(permission.getCode());
    }

    public boolean hasPermission(String roleId, String code) {
        Set<String> codes = role2PermissionCodeMap.get(roleId);
        return codes != null && codes.contains(code);
    }

    public Permission getPermission(String pid) {
        return permissionMap.get(pid);
    }

    public Collection<String> getPermissionCodeByRole(List<String> roleIds) {
        if (roleIds == null || roleIds.size() == 0) return Collections.EMPTY_LIST;
        if (roleIds.size() == 1) role2PermissionCodeMap.get(roleIds.get(0));
        Set<String> result = new HashSet<>();
        for (String roleId : roleIds) {
            Set<String> pSet = role2PermissionCodeMap.get(roleId);
            if (pSet != null) result.addAll(pSet);
        }
        return result;
    }

    public Collection<Permission> getPermissionByRole(List<String> roleIds) {
        if (roleIds == null || roleIds.size() == 0) return Collections.EMPTY_LIST;
        if (roleIds.size() == 1) role2PermissionMap.get(roleIds.get(0));
        Set<Permission> result = new HashSet<>();
        for (String roleId : roleIds) {
            Set<Permission> pSet = role2PermissionMap.get(roleIds);
            if (pSet != null) result.addAll(pSet);
        }
        return result;
    }

    public Collection<Permission> getAdminPermission() {
        return adminPermissionList;
    }

    public Collection<String> getAdminPermissionCode() {
        return adminPermissionCodeList;
    }


}
