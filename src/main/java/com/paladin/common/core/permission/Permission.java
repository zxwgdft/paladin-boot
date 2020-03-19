package com.paladin.common.core.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paladin.common.model.org.OrgPermission;
import com.paladin.framework.common.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基于shiro权限扩展
 *
 * @author TontoZhou
 * @since 2020/3/17
 */
@Getter
public class Permission extends WildcardPermission {

    private String id;

    private OrgPermission source;

    // 是否菜单
    private boolean isMenu;

    // 是否系统管理员权限
    private boolean isAdmin;

    @JsonIgnore
    private Permission parent;

    private List<Permission> children;

    // 最近的一个父辈菜单权限
    private Permission parentMenuPermission;

    // 父辈权限路径
    @Getter(AccessLevel.NONE)
    private String parentsPath;

    // 是否根权限
    private boolean isRoot;

    // 是否根菜单权限
    private boolean isRootMenu;

    // 是否叶节点
    private boolean isLeaf;

    // 是否可授权
    private boolean grantable;

    public Permission(OrgPermission source) {
        super(source.getCode());

        this.id = source.getId();
        this.source = source;
        this.isMenu = source.getIsMenu() == BaseModel.BOOLEAN_YES;
        this.isAdmin = source.getIsAdmin() == BaseModel.BOOLEAN_YES;
        this.grantable = source.getGrantable() == BaseModel.BOOLEAN_YES;
        this.children = new ArrayList<>();
    }

    /**
     * 初始化权限数据，应该在所有权限的父级与子级都设置完毕后调用
     */
    protected void init() {

        Collections.sort(children, (p1, p2) -> {
            return p1.source.getListOrder() - p2.source.getListOrder();
        });

        children = Collections.unmodifiableList(children);
        StringBuilder sb = new StringBuilder();
        Permission lastMenuPermission = null;

        // 获取父辈权限路径与最近一个父辈菜单权限
        Permission p = parent;
        while (p != null) {
            sb.append(p.id).append(",");
            if (p.isMenu && lastMenuPermission == null) {
                lastMenuPermission = p;
            }
            p = p.parent;
        }

        if (sb.length() > 0) {
            parentsPath = sb.toString();
        }

        this.parentMenuPermission = lastMenuPermission;
        this.isRoot = parent == null;
        this.isRootMenu = parentMenuPermission == null;
        this.isLeaf = children.size() == 0;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
    }

    /**
     * 是否是父辈元素
     *
     * @param id
     * @return
     */
    public boolean isParent(String id) {
        return parentsPath != null && parentsPath.contains(id);
    }


}
