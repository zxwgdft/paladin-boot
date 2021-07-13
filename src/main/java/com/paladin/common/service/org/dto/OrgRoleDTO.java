package com.paladin.common.service.org.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class OrgRoleDTO {

    // id
    private String id;

    // 角色名称
    @NotEmpty(message = "角色名称不能为空")
    private String roleName;

    // 角色等级（考核权限等级）
    @NotNull(message = "角色权限等级不能为空")
    private Integer roleLevel;

    // 角色说明
    private String roleDesc;

    // 是否启用 1是0否
    @NotNull(message = "是否启用不能为空")
    private Integer enable;


}