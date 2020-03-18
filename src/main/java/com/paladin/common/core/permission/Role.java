package com.paladin.common.core.permission;

import com.paladin.common.model.org.OrgRole;
import com.paladin.framework.common.BaseModel;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Role {

    private String id;

    private OrgRole source;

    // 角色名称
    private String roleName;

    // 角色等级
    private int roleLevel;

    // 是否默认角色
    private boolean isDefault;

    // 是否启用
    private boolean enable;


    private List<Menu> rootMenus;
    private List<Permission> permissions;

    @Getter(AccessLevel.NONE)
    private Set<String> permissionCodeSet;


    public Role(OrgRole orgRole) {
        if (orgRole != null) {
            this.source = orgRole;
            this.id = orgRole.getId();
            this.roleName = orgRole.getRoleName();
            this.roleLevel = orgRole.getRoleLevel();
            this.isDefault = orgRole.getIsDefault() == BaseModel.BOOLEAN_YES;
            this.enable = orgRole.getEnable() == BaseModel.BOOLEAN_YES;
        }
    }

    public void setPermission(List<Permission> ownedPermissions, List<Menu> rootMenus) {
        HashSet<String> permissionCodeSet = new HashSet<>();
        for (Permission permission : ownedPermissions) {
            permissionCodeSet.add(permission.getSource().getCode());
        }
        this.rootMenus = Collections.unmodifiableList(rootMenus);
        this.permissionCodeSet = Collections.unmodifiableSet(permissionCodeSet);
        this.permissions = Collections.unmodifiableList(ownedPermissions);
    }


    /**
     * 根据权限code判断是否有权限
     *
     * @param code
     * @return
     */
    public boolean hasPermission(String code) {
        return permissionCodeSet.contains(code);
    }


}
