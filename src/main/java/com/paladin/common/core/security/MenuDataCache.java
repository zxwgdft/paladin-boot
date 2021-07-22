package com.paladin.common.core.security;

import com.paladin.common.mapper.org.OrgMenuMapper;
import com.paladin.common.mapper.org.OrgRoleMenuMapper;
import com.paladin.common.model.org.OrgRoleMenu;
import com.paladin.framework.cache.DataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MenuDataCache implements DataCache<MenuContainer> {

    @Autowired
    private OrgMenuMapper orgMenuMapper;

    @Autowired
    private OrgRoleMenuMapper orgRoleMenuMapper;

    public String getId() {
        return "MENU_CACHE";
    }

    @Override
    public MenuContainer loadData(long version) {

        List<Menu> menus = orgMenuMapper.findMenu();

        int size = (int) (menus.size() / 0.75 + 1);

        Map<Integer, Menu> menuMap = new HashMap<>(size);
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
        }

        size = menus.size() / 2 + 1;
        List<Menu> rootMenus = new ArrayList<>(size);
        List<Menu> adminMenus = new ArrayList<>(size);

        for (Menu menu : menus) {
            Integer pid = menu.getParentId();
            if (pid != null) {
                Menu parentMenu = menuMap.get(pid);
                if (parentMenu != null) {
                    parentMenu.getChildren().add(menu);
                    menu.setParent(parentMenu);
                } else {
                    rootMenus.add(menu);
                }
            } else {
                rootMenus.add(menu);
            }
        }

        for (Menu menu : menus) {
            boolean isLeaf = menu.getChildren().isEmpty();
            menu.setLeaf(isLeaf);
            if (isLeaf) adminMenus.add(menu);
        }

        List<OrgRoleMenu> roleMenus = orgRoleMenuMapper.findList();

        Map<Integer, List<Menu>> roleMenuMap = new HashMap<>();
        for (OrgRoleMenu roleMenu : roleMenus) {

            Integer roleId = roleMenu.getRoleId();
            Integer menuId = roleMenu.getMenuId();

            Menu menu = menuMap.get(menuId);
            if (menu == null || !menu.isLeaf()) continue;

            List<Menu> menuSet = roleMenuMap.get(roleId);
            if (menuSet == null) {
                menuSet = new ArrayList<>();
                roleMenuMap.put(roleId, menuSet);
            }

            menuSet.add(menu);
        }

        Map<Integer, MenuToRole> roleMenuMap2 = new HashMap<>();
        for (Map.Entry<Integer, List<Menu>> entry : roleMenuMap.entrySet()) {
            roleMenuMap2.put(entry.getKey(), new MenuToRole(entry.getValue()));
        }

        return new MenuContainer(rootMenus, roleMenuMap2, new MenuToRole(adminMenus));
    }


}
