package com.paladin.common.core.security;

import com.paladin.common.mapper.org.OrgMenuMapper;
import com.paladin.common.mapper.org.OrgRoleMenuMapper;
import com.paladin.common.model.org.OrgRoleMenu;
import com.paladin.framework.cache.DataCache;
import com.paladin.framework.utils.StringUtil;
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

        Map<String, Menu> menuMap = new HashMap<>(size);
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
        }

        size = menus.size() / 2 + 1;
        List<Menu> rootMenus = new ArrayList<>(size);
        List<Menu> adminMenus = new ArrayList<>(size);

        for (Menu menu : menus) {
            String pid = menu.getParentId();
            if (StringUtil.isNotEmpty(pid)) {
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

        Map<String, List<Menu>> roleMenuMap = new HashMap<>();

        for (OrgRoleMenu roleMenu : roleMenus) {

            String roleId = roleMenu.getRoleId();
            String menuId = roleMenu.getMenuId();

            Menu menu = menuMap.get(menuId);
            if (menu == null || !menu.isLeaf()) continue;

            List<Menu> menuSet = roleMenuMap.get(roleId);
            if (menuSet == null) {
                menuSet = new ArrayList<>();
                roleMenuMap.put(roleId, menuSet);
            }

            menuSet.add(menu);
        }

        return new MenuContainer(rootMenus, roleMenuMap, adminMenus);
    }


}
