package com.paladin.common.model.org;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@ApiModel(description = "角色权限关联")
public class OrgRolePermission {

    @Id
    private String roleId;
    @Id
    private String permissionId;


}