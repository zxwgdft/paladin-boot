package com.paladin.common.core.security;

import java.util.*;

/**
 * @author TontoZhou
 * @since 2021/6/4
 */
public class MenuContainer {

    private Map<Integer, Menu> menuMap;
    private List<Menu> rootMenus;
    private MenuToRole adminMenus;
    private Map<Integer, MenuToRole> roleMenuMap;
    private Map<MultiRoleKey, MenuToRole> multiRoleMenuMap;

    public MenuContainer(Map<Integer, Menu> menuMap, List<Menu> rootMenus, Map<Integer, MenuToRole> roleMenuMap, MenuToRole adminMenus) {
        this.menuMap = menuMap;
        this.roleMenuMap = roleMenuMap;
        this.rootMenus = rootMenus;
        this.adminMenus = adminMenus;
        this.multiRoleMenuMap = new HashMap<>();
    }

    public Collection<Menu> getRoleMenus(List<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) return Collections.EMPTY_LIST;
        if (roleIds.size() == 1) {
            MenuToRole menuRole = roleMenuMap.get(roleIds.get(0));
            return menuRole == null ? Collections.EMPTY_LIST : menuRole.getRootMenus();
        }

        MultiRoleKey key = new MultiRoleKey(roleIds);
        MenuToRole menuToRole = multiRoleMenuMap.get(key);
        if (menuToRole == null) {
            Set<Menu> menus = new HashSet<>();
            for (Integer roleId : key.roleIds) {
                MenuToRole menuRole = roleMenuMap.get(roleId);
                if (menuRole != null) {
                    menus.addAll(menuRole.getLeafMenus());
                }
            }

            List<Menu> leafMenuList = new ArrayList<>(menus);
            menuToRole = new MenuToRole(leafMenuList);
            multiRoleMenuMap.put(key, menuToRole);
        }
        return menuToRole.getRootMenus();
    }

    public Collection<Menu> getRootMenus() {
        return rootMenus;
    }

    public Collection<Menu> getAdminMenus() {
        return adminMenus.getRootMenus();
    }

    public Menu getMenu(int id){
        return menuMap.get(id);
    }

    private static class MultiRoleKey {

        private Set<Integer> roleIds;

        private MultiRoleKey(List<Integer> roleIdList) {
            if (roleIdList == null || roleIdList.size() == 0) {
                throw new RuntimeException("can't be empty list");
            }
            roleIds = new HashSet<>();
            for (Integer roleId : roleIdList) {
                if (roleId != null) {
                    roleIds.add(roleId);
                }
            }
            if (roleIds.size() == 0) {
                throw new RuntimeException("can't be empty list");
            }
        }

        public int hashCode() {
            int hash = 0;
            for (Integer roleId : roleIds) {
                hash += roleId;
            }
            // 减少相加带来的hash碰撞
            return hash << (roleIds.size() - 1);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof MultiRoleKey) {
                MultiRoleKey mr = (MultiRoleKey) obj;
                if (roleIds.size() == mr.roleIds.size()) {
                    for (Integer s : roleIds) {
                        if (!mr.roleIds.contains(s)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }
}
