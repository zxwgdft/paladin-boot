package com.paladin.common.core.security;

import java.util.*;

/**
 * @author TontoZhou
 * @since 2021/6/4
 */
public class MenuContainer {

    private List<Menu> rootMenus;
    private List<Menu> adminMenus;
    private Map<String, List<Menu>> roleMenuMap;

    public MenuContainer(List<Menu> rootMenus, Map<String, List<Menu>> roleMenuMap, List<Menu> adminMenus) {
        this.roleMenuMap = roleMenuMap;
        this.rootMenus = rootMenus;
        this.adminMenus = adminMenus;
    }

    public Collection<Menu> getRoleMenus(List<String> roleIds) {
        if (roleIds == null || roleIds.size() == 0) return Collections.EMPTY_LIST;
        if (roleIds.size() == 1) return roleMenuMap.get(roleIds.get(0));

        Set<Menu> menus = new HashSet<>();
        for (String roleId : roleIds) {
            List<Menu> ms = roleMenuMap.get(roleId);
            if (ms != null) {
                menus.addAll(ms);
            }
        }
        return menus;
    }

    public Collection<Menu> getRootMenus() {
        return rootMenus;
    }

    public Collection<Menu> getAdminMenus() {
        return adminMenus;
    }
}
