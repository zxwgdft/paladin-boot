package com.paladin.common.core.security;

import java.util.Map;
import java.util.Set;

/**
 * @author TontoZhou
 * @since 2021/3/23
 */
public class PermissionContainer {

    // role to permission code set
    private Map<String, Set<String>> role2PermissionCodeMap;

    // permission id to permission
    private Map<String, Permission> permissionMap;

    public PermissionContainer(Map<String, Permission> permissionMap, Map<String, Set<String>> role2PermissionCodeMap) {
        this.role2PermissionCodeMap = role2PermissionCodeMap;
        this.permissionMap = permissionMap;
    }

    public Set<String> getPermissionCodeByRole(String roleId) {
        return role2PermissionCodeMap.get(roleId);
    }

    public boolean hasPermission(String roleId, String code) {
        Set<String> codes = role2PermissionCodeMap.get(roleId);
        return codes != null && codes.contains(code);
    }

    public Permission getPermission(String pid) {
        return permissionMap.get(pid);
    }

}
