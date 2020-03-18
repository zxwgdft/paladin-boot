package com.paladin.common.core.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.common.model.org.OrgPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Menu {

    private OrgPermission source;

    private String id;
    // 是否拥有
    private boolean owned;

    @JsonIgnore
    private Menu parent;

    private List<Menu> children;

    public Menu(Permission permission, boolean owned) {
        this.id = permission.getId();
        this.source = permission.getSource();
        this.owned = owned;
        this.children = new ArrayList<>();
    }




}
