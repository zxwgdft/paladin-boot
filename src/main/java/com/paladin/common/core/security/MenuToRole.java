package com.paladin.common.core.security;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2021/7/2
 */
@Getter
@Setter
public class MenuToRole {

    private List<Menu> leafMenus;
    private List<Menu> rootMenus;

    MenuToRole(List<Menu> leafMenus) {
        this.leafMenus = leafMenus;
        this.rootMenus = getRootMenu(leafMenus);
    }

    private List<Menu> getRootMenu(List<Menu> leafMenus) {
        if (leafMenus == null || leafMenus.size() == 0) return leafMenus;

        leafMenus.sort((o1, o2) -> {
            return getOrderNo(o1) - getOrderNo(o2);
        });

        List<Menu> result = new ArrayList<>();
        Map<Integer, Menu> categoryMap = new HashMap<>();

        for (Menu leaf : leafMenus) {
            Menu parent = leaf.getParent();
            if (parent == null) {
                result.add(leaf);
            } else {
                Integer pid = parent.getId();
                Menu category = categoryMap.get(pid);
                if (category == null) {
                    boolean first = true;
                    do {
                        category = new Menu();
                        category.setId(pid);
                        category.setName(parent.getName());
                        category.setLeaf(false);
                        categoryMap.put(pid, category);

                        if (first) {
                            category.getChildren().add(leaf);
                            first = false;
                        }

                        parent = parent.getParent();
                        if (parent == null) {
                            result.add(category);
                            break;
                        }
                        pid = parent.getId();
                        category = categoryMap.get(pid);
                        if (category != null) break;
                    } while (true);
                } else {
                    category.getChildren().add(leaf);
                }
            }
        }

        return result;
    }

    private int getOrderNo(Menu menu) {
        int i = menu.getOrderNo();
        menu = menu.getParent();
        if (menu != null) {
            i += (menu.getOrderNo() << 8);
            menu = menu.getParent();
            if (menu != null) {
                i += (menu.getOrderNo() << 16);
                menu = menu.getParent();
                if (menu != null) {
                    i += (menu.getOrderNo() << 24);
                }
            }
        }
        return i;
    }


}
