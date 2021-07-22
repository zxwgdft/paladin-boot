package com.paladin.common.model.org;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class OrgRolePermission {

    @Id
    private Integer roleId;
    @Id
    private Integer permissionId;


}