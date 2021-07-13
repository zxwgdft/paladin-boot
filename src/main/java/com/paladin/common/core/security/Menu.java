package com.paladin.common.core.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.framework.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Menu implements Serializable {

    private String id;

    private String url;

    private String name;

    private String icon;

    private String parentId;

    private boolean isLeaf;

    @JsonIgnore
    private Menu parent;

    private List<Menu> children = new ArrayList<>();

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Menu) {
            return StringUtil.equals(id, ((Menu) obj).id);
        }
        return false;
    }

    public int hashCode() {
        return 17 * 31 + id.hashCode();
    }

}
