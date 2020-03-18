package com.paladin.common.core.permission;

import com.paladin.common.model.org.OrgPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Menu {

    private String id;

    private String url;

    private String name;

    private String icon;

    private boolean owned;

    private boolean isRoot;

    private List<Menu> children;

    public Menu(Permission permission, boolean owned) {
        this.id = permission.getId();
        OrgPermission source = permission.getSource();
        this.url = source.getUrl();
        this.name = source.getName();
        this.icon = source.getMenuIcon();
        this.owned = owned;
        this.isRoot = permission.isRootMenu();
        this.children = new ArrayList<>();
    }


}
