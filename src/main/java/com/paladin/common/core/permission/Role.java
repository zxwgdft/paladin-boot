package com.paladin.common.core.permission;

import com.paladin.common.model.org.OrgRole;
import com.paladin.framework.common.BaseModel;
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
    private Set<String> permissionCodes;

    // 默认角色、系统管理员
    public Role(String id, String roleName, int roleLevel) {
        this.id = id;
        this.roleName = roleName;
        this.isDefault = true;
        this.enable = true;
        this.roleLevel = roleLevel;
    }

    public Role(OrgRole orgRole) {
        this.source = orgRole;
        this.id = orgRole.getId();
        this.roleName = orgRole.getRoleName();
        this.roleLevel = orgRole.getRoleLevel();
        this.isDefault = orgRole.getIsDefault() == BaseModel.BOOLEAN_YES;
        this.enable = orgRole.getEnable() == BaseModel.BOOLEAN_YES;
    }

    public void setPermission(List<Permission> ownedPermissions, List<Menu> rootMenus) {
        HashSet<String> permissionCodeSet = new HashSet<>();
        for (Permission permission : ownedPermissions) {
            permissionCodeSet.add(permission.getSource().getCode());
        }
        this.rootMenus = Collections.unmodifiableList(rootMenus);
        this.permissionCodes = Collections.unmodifiableSet(permissionCodeSet);
        this.permissions = Collections.unmodifiableList(ownedPermissions);
    }


    /**
     * 根据权限code判断是否有权限
     *
     * @param code
     * @return
     */
    public boolean hasPermission(String code) {
        return permissionCodes.contains(code);
    }


}
