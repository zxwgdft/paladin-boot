package com.paladin.common.service.org.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgRoleVO {

    // id
    private String id;

    // 角色名称
    private String roleName;

    // 角色等级（考核权限等级）
    private Integer roleLevel;

    // 角色说明
    private String roleDesc;

    // 是否默认角色（1是0否）
    private Integer isDefault;

    // 是否启用 1是0否
    private Integer enable;


}