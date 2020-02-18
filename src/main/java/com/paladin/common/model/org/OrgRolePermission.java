package com.paladin.common.model.org;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
public class OrgRolePermission {

    // 角色ID
    @Id
    private String roleId;

    // 权限ID
    @Id
    private String permissionId;


}