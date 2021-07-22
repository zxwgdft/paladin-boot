package com.paladin.common.core.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Menu implements Serializable {

    private int id;

    private String url;

    private String name;

    private String icon;

    private Integer parentId;

    private boolean isLeaf;

    private int orderNo;

    @JsonIgnore
    private Menu parent;

    private List<Menu> children = new ArrayList<>();

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Menu) {
            return id == ((Menu) obj).id;
        }
        return false;
    }

    public int hashCode() {
        return 17 * 31 + id;
    }

}
