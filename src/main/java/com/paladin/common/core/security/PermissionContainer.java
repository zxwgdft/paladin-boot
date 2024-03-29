package com.paladin.common.core.security;

import java.util.*;

/**
 * @author TontoZhou
 * @since 2021/3/23
 */
public class PermissionContainer {

    // role to permission code set
    private Map<Integer, Set<String>> role2PermissionCodeMap;
    // role to permission set
    private Map<Integer, Set<CodePermission>> role2PermissionMap;
    // permission code to permission
    private Map<String, CodePermission> code2permissionMap;
    // permission id to permission
    private Map<Integer, CodePermission> id2permissionMap;

    private List<CodePermission> adminCodePermissionList;
    private List<String> adminPermissionCodeList;

    public PermissionContainer(Map<Integer, CodePermission> id2permissionMap, Map<String, CodePermission> code2permissionMap, Map<Integer, Set<String>> role2PermissionCodeMap, Map<Integer, Set<CodePermission>> role2PermissionMap) {
        this.role2PermissionCodeMap = role2PermissionCodeMap;
        this.role2PermissionMap = role2PermissionMap;
        this.code2permissionMap = code2permissionMap;
        this.id2permissionMap = id2permissionMap;

        this.adminCodePermissionList = new ArrayList<>(code2permissionMap.values());
        this.adminPermissionCodeList = new ArrayList<>(adminCodePermissionList.size());

        for (CodePermission codePermission : adminCodePermissionList) {
            if(codePermission.isAdmin()) {
                adminPermissionCodeList.add(codePermission.getCode());
            }
        }
    }

    public boolean hasPermission(int roleId, String code) {
        Set<String> codes = role2PermissionCodeMap.get(roleId);
        return codes != null && codes.contains(code);
    }

    public CodePermission getPermission(String code) {
        return code2permissionMap.get(code);
    }

    public CodePermission getPermission(int id) {
        return id2permissionMap.get(id);
    }

    public Collection<String> getPermissionCodeByRole(List<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) return Collections.EMPTY_LIST;
        if (roleIds.size() == 1) role2PermissionCodeMap.get(roleIds.get(0));
        Set<String> result = new HashSet<>();
        for (Integer roleId : roleIds) {
            Set<String> pSet = role2PermissionCodeMap.get(roleId);
            if (pSet != null) result.addAll(pSet);
        }
        return result;
    }

    public Collection<CodePermission> getPermissionByRole(List<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) return Collections.EMPTY_LIST;
        if (roleIds.size() == 1) role2PermissionMap.get(roleIds.get(0));
        Set<CodePermission> result = new HashSet<>();
        for (Integer roleId : roleIds) {
            Set<CodePermission> pSet = role2PermissionMap.get(roleIds);
            if (pSet != null) result.addAll(pSet);
        }
        return result;
    }

    public Collection<CodePermission> getAdminPermission() {
        return adminCodePermissionList;
    }

    public Collection<String> getAdminPermissionCode() {
        return adminPermissionCodeList;
    }


}
